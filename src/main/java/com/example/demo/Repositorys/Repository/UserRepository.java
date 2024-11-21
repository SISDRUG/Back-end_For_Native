package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Long> {

}
