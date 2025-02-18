package com.example.demo.Services;

import com.example.demo.Repositorys.Entity.Currency;
import com.example.demo.Repositorys.Repository.CurrencyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final ObjectMapper objectMapper;

    public Currency getCurrencyById(Long id) {
        return currencyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public Currency patchCurrency (Long id, JsonNode patchNode) throws IOException {
        Currency currency = currencyRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
        objectMapper.readerForUpdating(currency).readValue(patchNode);
        return currencyRepository.save(currency);
    }
}
