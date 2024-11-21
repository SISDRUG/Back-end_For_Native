package com.example.demo.Services;


import com.example.demo.Repositorys.Entity.Card;
import com.example.demo.Repositorys.Repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    @Autowired
    private CardRepository articleRepository;

    public List<Card> getCards() {
        return articleRepository.findAll();
    }
}
