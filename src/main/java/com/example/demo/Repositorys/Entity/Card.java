package com.example.demo.Repositorys.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "cards", indexes = {
        @Index(name = "card_number", columnList = "card_number", unique = true),
        @Index(name = "idx_cards_package", columnList = "package_id")
})
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "balance")
    private Long balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "package_id")
    private Package packageField;

    @NotNull
    @Column(name = "year_of_exp", nullable = false)
    private LocalDate yearOfExp;

    @NotNull
    @Column(name = "month_of_exp", nullable = false)
    private LocalDate monthOfExp;

    @NotNull
    @Column(name = "card_number", nullable = false)
    private Long cardNumber;

    @Size(max = 20)
    @Column(name = "card_status", length = 20)
    private String cardStatus;

    @Size(max = 150)
    @NotNull
    @Column(name = "holder_name", nullable = false, length = 150)
    private String holderName;

    @Column(name = "is_virtual")
    private Boolean isVirtual;

}