package com.example.demo.Controllers;
import com.example.demo.Repositorys.Entity.Currency;
import com.example.demo.Repositorys.Repository.CurrencyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin-ui/currencies")
@RequiredArgsConstructor
public class CurrencyResource {

    private final CurrencyRepository currencyRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Currency> getAll(@ParameterObject Pageable pageable) {
        Page<Currency> currencies = currencyRepository.findAll(pageable);
        return new PagedModel<>(currencies);
    }

    @GetMapping("/{id}")
    public Currency getOne(@PathVariable Long id) {
        Optional<Currency> currencyOptional = currencyRepository.findById(id);
        return currencyOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Currency> getMany(@RequestParam List<Long> ids) {
        return currencyRepository.findAllById(ids);
    }

    @PostMapping
    public Currency create(@RequestBody @Valid Currency currency) {
        return currencyRepository.save(currency);
    }

    @PatchMapping("/{id}")
    public Currency patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Currency currency = currencyRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(currency).readValue(patchNode);

        return currencyRepository.save(currency);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Currency> currencies = currencyRepository.findAllById(ids);

        for (Currency currency : currencies) {
            objectMapper.readerForUpdating(currency).readValue(patchNode);
        }

        List<Currency> resultCurrencies = currencyRepository.saveAll(currencies);
        return resultCurrencies.stream()
                .map(Currency::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Currency delete(@PathVariable Long id) {
        Currency currency = currencyRepository.findById(id).orElse(null);
        if (currency != null) {
            currencyRepository.delete(currency);
        }
        return currency;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        currencyRepository.deleteAllById(ids);
    }
}
