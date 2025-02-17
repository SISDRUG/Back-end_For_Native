package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.Card;
import com.example.demo.Repositorys.Repository.CardRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin-ui/cards")
@RequiredArgsConstructor
public class CardResource {

    private final CardRepository cardRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Card> getAll(@ParameterObject Pageable pageable) {
        Page<Card> cards = cardRepository.findAll(pageable);
        return new PagedModel<>(cards);
    }

    @GetMapping("/{id}")
    public Card getOne(@PathVariable Long id) {
        Optional<Card> cardOptional = cardRepository.findById(id);
        return cardOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Card> getMany(@RequestParam List<Long> ids) {
        return cardRepository.findAllById(ids);
    }

    @PostMapping
    public Card create(@RequestBody @Valid Card card) {
        return cardRepository.save(card);
    }

    @PatchMapping("/{id}")
    public Card patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Card card = cardRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(card).readValue(patchNode);

        return cardRepository.save(card);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Card> cards = cardRepository.findAllById(ids);

        for (Card card : cards) {
            objectMapper.readerForUpdating(card).readValue(patchNode);
        }

        List<Card> resultCards = cardRepository.saveAll(cards);
        return resultCards.stream()
                .map(Card::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Card delete(@PathVariable Long id) {
        Card card = cardRepository.findById(id).orElse(null);
        if (card != null) {
            cardRepository.delete(card);
        }
        return card;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        cardRepository.deleteAllById(ids);
    }
}
