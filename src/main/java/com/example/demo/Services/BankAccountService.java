package com.example.demo.Services;

import com.example.demo.Repositorys.Repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BankAccountService{
    private final BankAccountRepository bankAccountRepository;



}
