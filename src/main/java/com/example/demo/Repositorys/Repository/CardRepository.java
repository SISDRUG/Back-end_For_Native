package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}