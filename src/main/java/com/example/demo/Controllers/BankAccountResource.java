package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.BankAccount;
import com.example.demo.Repositorys.Entity.Currency;
import com.example.demo.Repositorys.Repository.BankAccountRepository;
import com.example.demo.Repositorys.Repository.CurrencyRepository;
import com.example.demo.Services.BankAccountService;
import com.fasterxml.jackson.databind.JsonNode;
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

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/rest/admin-ui/bankAccounts")
@RequiredArgsConstructor
public class BankAccountResource {

    private final BankAccountRepository bankAccountRepository;
    private final CurrencyRepository currencyRepository;

    private final BankAccountService bankAccountService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<BankAccount> getAll(@ParameterObject Pageable pageable) {
        Page<BankAccount> bankAccounts = bankAccountRepository.findAll(pageable);
        return new PagedModel<>(bankAccounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getOne(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountService.getAccountById(id);
        return ResponseEntity.ok(bankAccount);
    }

    @GetMapping("/by-ids")
    public List<BankAccount> getMany(@RequestParam List<Long> ids) {
        return bankAccountRepository.findAllById(ids);
    }

    /// TODO Перенести логику в сервис
    @PostMapping
    public ResponseEntity<BankAccount> create(@RequestBody @Valid JsonNode patchNode) throws Exception {
        BankAccount bankAccount = new BankAccount();
        objectMapper.readerForUpdating(bankAccount).readValue(patchNode);
        Long currencyId = patchNode.path("currencyId").asLong();
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new Exception("Currency not found for ID: " + currencyId));
        bankAccount.setCurrency(currency);

        return ResponseEntity.ok(bankAccountRepository.save(bankAccount));
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
    public BankAccount delete(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElse(null);
        if (bankAccount != null) {
            bankAccountRepository.delete(bankAccount);
        }
        return bankAccount;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        bankAccountRepository.deleteAllById(ids);
    }
}
