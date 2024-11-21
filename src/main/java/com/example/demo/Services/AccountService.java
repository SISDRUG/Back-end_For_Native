package com.example.demo.Services;

import com.example.demo.Repositorys.Entity.Account;
import com.example.demo.Repositorys.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

        @Autowired
        private AccountRepository articleRepository;

        public List<Account> getAccounts() {
            return articleRepository.findAll();
        }
}
