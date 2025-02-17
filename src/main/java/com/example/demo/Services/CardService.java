package com.example.demo.Services;

import com.example.demo.Repositorys.Entity.Card;
import com.example.demo.Repositorys.Repository.CardRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final ObjectMapper objectMapper;

    public Card getCardById(Long id) {
        return cardRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public Card patchCard (Long id, JsonNode patchNode) throws IOException {
        Card card = cardRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
        objectMapper.readerForUpdating(card).readValue(patchNode);
        return cardRepository.save(card);
    }
}
