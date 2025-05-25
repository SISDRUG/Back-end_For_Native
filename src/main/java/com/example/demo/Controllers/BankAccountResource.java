package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.BankAccount;
import com.example.demo.Repositorys.Entity.Currency;
import com.example.demo.Repositorys.Repository.BankAccountRepository;
import com.example.demo.Repositorys.Repository.CurrencyRepository;
import com.example.demo.Services.BankAccountService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.Repositorys.Repository.BankAccountsCredentialRepository;
import com.example.demo.Repositorys.Entity.BankAccountsCredential;
import com.example.demo.Repositorys.Repository.CredentialRepository;
import com.example.demo.Repositorys.Entity.Credential;
import com.example.demo.Repositorys.Entity.BankAccountsCredentialId;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/rest/admin-ui/bankAccounts")
@RequiredArgsConstructor
public class BankAccountResource {
    private static final Logger logger = LoggerFactory.getLogger(BankAccountResource.class);

    private final BankAccountRepository bankAccountRepository;
    private final CurrencyRepository currencyRepository;
    private final BankAccountsCredentialRepository bankAccountsCredentialRepository;
    private final CredentialRepository credentialRepository;

    private final BankAccountService bankAccountService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<BankAccount> getAll(@ParameterObject Pageable pageable) {
        Page<BankAccount> bankAccounts = bankAccountRepository.findAll(pageable);
        return new PagedModel<>(bankAccounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getOne(@PathVariable Long id) {
        logger.info("Getting bank account with id: {}", id);
        BankAccount bankAccount = bankAccountService.getAccountById(id);
        return ResponseEntity.ok(bankAccount);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<BankAccount>> getByUserId(@PathVariable Long userId) {
        logger.info("Getting bank accounts for user id: {} (via credentials)", userId);
        List<BankAccount> accounts = bankAccountsCredentialRepository.findAllByUserId(userId)
            .stream()
            .map(BankAccountsCredential::getBankAccount)
            .map(account -> {
                // Инициализируем все необходимые поля
                if (account.getCurrency() != null) {
                    account.getCurrency().getCurAbbreviation();
                    account.getCurrency().getCurScale();
                    account.getCurrency().getCurRate();
                }
                return account;
            })
            .toList();
        logger.info("Found {} accounts for user {}", accounts.size(), userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/by-ids")
    public List<BankAccount> getMany(@RequestParam List<Long> ids) {
        logger.info("Getting bank accounts by ids: {}", ids);
        return bankAccountRepository.findAllById(ids);
    }

    /// TODO Перенести логику в сервис
    @PostMapping
    public ResponseEntity<BankAccount> create(@RequestBody @Valid JsonNode patchNode) throws Exception {
        // Получаем credentialId из запроса до создания объекта BankAccount
        Long credentialId = patchNode.path("credentialId").asLong();
        if (credentialId == null || credentialId == 0) {
            throw new Exception("credentialId is required for account creation");
        }
        
        // Создаем и заполняем объект BankAccount
        BankAccount bankAccount = new BankAccount();
        // Удаляем credentialId из patchNode перед десериализацией
        ((ObjectNode) patchNode).remove("credentialId");
        objectMapper.readerForUpdating(bankAccount).readValue(patchNode);
        
        // Получаем ID валюты из объекта currency
        Long currencyId = patchNode.path("currency").path("id").asLong();
        if (currencyId == 0) {
            throw new Exception("Currency ID is required");
        }
        
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new Exception("Currency not found for ID: " + currencyId));
        bankAccount.setCurrency(currency);

        // Устанавливаем дату создания и дату последней операции
        bankAccount.setDateOfCreation(java.time.LocalDate.now());
        bankAccount.setLastOperationDate(java.time.Instant.now());

        // Сохраняем счет
        BankAccount savedAccount = bankAccountRepository.save(bankAccount);

        // Получаем credential
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new Exception("Credential not found for ID: " + credentialId));

        // Создаем связь
        BankAccountsCredentialId bacId = new BankAccountsCredentialId();
        bacId.setBankAccountId(savedAccount.getId());
        bacId.setCredentialsId(credential.getId());
        BankAccountsCredential bac = new BankAccountsCredential();
        bac.setId(bacId);
        bac.setBankAccount(savedAccount);
        bac.setCredentials(credential);
        bankAccountsCredentialRepository.save(bac);

        return ResponseEntity.ok(savedAccount);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BankAccount> patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws Exception {
        BankAccount bankAccount = bankAccountService.patchAccount(id,patchNode);
        return ResponseEntity.ok(bankAccount);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<BankAccount> bankAccounts = bankAccountRepository.findAllById(ids);

        for (BankAccount bankAccount : bankAccounts) {
            objectMapper.readerForUpdating(bankAccount).readValue(patchNode);
        }

        List<BankAccount> resultBankAccounts = bankAccountRepository.saveAll(bankAccounts);
        return resultBankAccounts.stream()
                .map(BankAccount::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<BankAccount> delete(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank account not found"));
        
        // Удаляем связанные записи из bank_accounts_credentials
        bankAccountsCredentialRepository.deleteAll(
            bankAccountsCredentialRepository.findAllByBankAccountId(id)
        );
        
        // Удаляем счет
        bankAccountRepository.delete(bankAccount);
        
        return ResponseEntity.ok(bankAccount);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        bankAccountRepository.deleteAllById(ids);
    }
}
