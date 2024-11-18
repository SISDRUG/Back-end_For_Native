package com.example.demo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @JsonBackReference
    private Account account;  // Связь с аккаунтом
    private Long userId;
    private LocalDate dateExp;
    private BigDecimal value;
    //   private List<Operation> operations;  // Предполагается, что Operation - это другой класс

    public Card() {

    }

    public Card(Long id, Account account, Long userId, LocalDate dateExp, BigDecimal value) {
        this.id = id;
        this.account = account;
        this.userId = userId;
        this.dateExp = dateExp;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getDateExp() {
        return dateExp;
    }

    public void setDateExp(LocalDate dateExp) {
        this.dateExp = dateExp;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
