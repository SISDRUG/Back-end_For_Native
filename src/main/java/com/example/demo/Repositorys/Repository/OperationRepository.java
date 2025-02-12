package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}