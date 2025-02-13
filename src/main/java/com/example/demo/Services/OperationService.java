package com.example.demo.Services;

import com.example.demo.Repositorys.Repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OperationService{

    @Autowired
    private OperationRepository operationRepository;
}
