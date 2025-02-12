package com.example.demo.Repositorys.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "bank_accounts", indexes = {
        @Index(name = "idx_bank_accounts_currency", columnList = "currency_id")
})
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "balance")
    private Long balance;

    @Size(max = 50)
    @NotNull
    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "date_of_creation")
    private LocalDate dateOfCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "account_status")
    private Boolean accountStatus;

    @Column(name = "last_operation_date")
    private Instant lastOperationDate;

}