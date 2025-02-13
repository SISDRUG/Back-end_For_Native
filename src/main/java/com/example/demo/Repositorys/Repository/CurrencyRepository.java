package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}