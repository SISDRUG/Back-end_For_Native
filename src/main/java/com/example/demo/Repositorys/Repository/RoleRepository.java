package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}