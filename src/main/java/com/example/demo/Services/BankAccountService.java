package com.example.demo.Services;

import com.example.demo.Repositorys.Entity.BankAccount;
import com.example.demo.Repositorys.Repository.BankAccountRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class BankAccountService{
    private final BankAccountRepository bankAccountRepository;
    private final ObjectMapper objectMapper;

    public BankAccount getAccountById(Long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public BankAccount patchAccount (Long id, JsonNode patchNode) throws IOException {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
        objectMapper.readerForUpdating(bankAccount).readValue(patchNode);
        return bankAccountRepository.save(bankAccount);
    }

}
