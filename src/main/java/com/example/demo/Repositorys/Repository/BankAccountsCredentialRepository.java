package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.BankAccountsCredential;
import com.example.demo.Repositorys.Entity.BankAccountsCredentialId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountsCredentialRepository extends JpaRepository<BankAccountsCredential, BankAccountsCredentialId> {
}