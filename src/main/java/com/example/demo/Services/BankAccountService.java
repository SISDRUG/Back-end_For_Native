package com.example.demo.Services;

import com.example.demo.Repositorys.Entity.BankAccount;
import com.example.demo.Repositorys.Repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BankAccountService{
    private final BankAccountRepository bankAccountRepository;

}
