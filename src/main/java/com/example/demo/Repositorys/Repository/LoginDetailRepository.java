package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.LoginDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginDetailRepository extends JpaRepository<LoginDetail, Long> {
}