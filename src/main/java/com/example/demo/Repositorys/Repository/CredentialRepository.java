package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
}