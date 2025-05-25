package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    @Modifying
    @Query("DELETE FROM Credential c WHERE c.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    Optional<Credential> findByEmailId(Long loginId);
}