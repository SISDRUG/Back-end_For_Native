package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.Card;
import com.example.demo.Services.CardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Карта")
public class CardController {

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    private final CardService cardService;

    @GetMapping("/cards")
    public List<Card> getAllCards() {
        return cardService.getCards();
    }
}
