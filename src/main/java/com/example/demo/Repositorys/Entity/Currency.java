package com.example.demo.Repositorys.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 10)
    @NotNull
    @Column(name = "cur_abbreviation", nullable = false, length = 10)
    private String curAbbreviation;

    @NotNull
    @Column(name = "cur_scale", nullable = false)
    private Long curScale;

    @NotNull
    @Column(name = "cur_rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal curRate;

}