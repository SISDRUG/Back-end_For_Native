package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.Currency;
import com.example.demo.Repositorys.Repository.CurrencyRepository;
import com.example.demo.Services.CurrencyService;
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
@RequestMapping("/rest/admin-ui/currencies")
@RequiredArgsConstructor
public class CurrencyResource {

    private final CurrencyRepository currencyRepository;

    private final ObjectMapper objectMapper;

    private final CurrencyService currencyService;

    @GetMapping
    public PagedModel<Currency> getAll(@ParameterObject Pageable pageable) {
        Page<Currency> currencies = currencyRepository.findAll(pageable);
        return new PagedModel<>(currencies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getOne(@PathVariable Long id) {
        Currency currency = currencyService.getCurrencyById(id);
        return ResponseEntity.ok(currency);
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
    public ResponseEntity<Currency> patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Currency currency = currencyService.patchCurrency(id,patchNode);
        return ResponseEntity.ok(currency);
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
