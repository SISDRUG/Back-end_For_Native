package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private AccountService accountService;

    @GetMapping("/getall/accounts")
    public List<Account> getAllAccounts() {
        return accountService.getAccounts();
    }

    @Autowired
    private CardService cardService;

    @GetMapping("/getall/cards")
    public List<Card> getAllCards() {
        return cardService.getCards();
    }
}