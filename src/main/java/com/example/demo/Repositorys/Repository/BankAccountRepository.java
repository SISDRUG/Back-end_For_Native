package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}