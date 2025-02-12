package com.example.demo.Services;

import com.example.demo.Repositorys.Repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BankAccountService{
    @Autowired
    private BankAccountRepository bankAccountRepository;
}
