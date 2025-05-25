package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.BankAccountsCredential;
import com.example.demo.Repositorys.Entity.BankAccountsCredentialId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BankAccountsCredentialRepository extends JpaRepository<BankAccountsCredential, BankAccountsCredentialId> {
    @Query("SELECT bac FROM BankAccountsCredential bac WHERE bac.credentials.user.id = :userId")
    List<BankAccountsCredential> findAllByUserId(@Param("userId") Long userId);

    List<BankAccountsCredential> findAllByBankAccountId(Long bankAccountId);
}