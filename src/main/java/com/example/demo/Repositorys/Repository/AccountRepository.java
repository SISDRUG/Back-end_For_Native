package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository <Account, Long> {

}
