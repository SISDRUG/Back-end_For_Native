package com.example.demo.Repositorys.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "operations", indexes = {
        @Index(name = "idx_operations_date", columnList = "date_time"),
        @Index(name = "reference_number", columnList = "reference_number", unique = true),
        @Index(name = "idx_operations_card", columnList = "card_id"),
        @Index(name = "idx_operations_currency", columnList = "currency_id")
})
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "recipient_details")
    private Long recipientDetails;

    @NotNull
    @Column(name = "value", nullable = false)
    private Long value;

    @Column(name = "date_time")
    private Instant dateTime;

    @Size(max = 50)
    @NotNull
    @Column(name = "operation_type", nullable = false, length = 50)
    private String operationType;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "reference_number", nullable = false)
    private Long referenceNumber;

}