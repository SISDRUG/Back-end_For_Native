package com.example.demo.Services;

import com.example.demo.Repositorys.Entity.BankAccount;
import com.example.demo.Repositorys.Entity.Currency;
import com.example.demo.Repositorys.Repository.BankAccountRepository;
import com.example.demo.Repositorys.Repository.CurrencyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class BankAccountService{
    private final BankAccountRepository bankAccountRepository;
    private final ObjectMapper objectMapper;
    private final CurrencyRepository currencyRepository;


    public BankAccount getAccountById(Long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public BankAccount patchAccount (Long id, JsonNode patchNode) throws Exception {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
        objectMapper.readerForUpdating(bankAccount).readValue(patchNode);
        Long currencyId = patchNode.path("currencyId").asLong();
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new Exception("Currency not found for ID: " + currencyId));
        bankAccount.setCurrency(currency);
        bankAccount.setLastOperationDate(Instant.now());
        return bankAccountRepository.save(bankAccount);
    }


    public BankAccount createBankAccount(BankAccount bankAccount) {
        if (bankAccount.getDateOfCreation() == null) {
            bankAccount.setDateOfCreation(LocalDate.from(LocalDateTime.now()));
        }

        if (bankAccount.getAccountStatus() == null) {
            bankAccount.setAccountStatus(false);
        }

        if (bankAccount.getLastOperationDate() == null){
            bankAccount.setLastOperationDate(Instant.now());
        }

//        if (bankAccount.getCurrency() == null){
//            Currency currency = currencyRepository.getReferenceById(2L);
//            bankAccount.setCurrency(currency);
//        }


        return bankAccountRepository.save(bankAccount);
    }
}
