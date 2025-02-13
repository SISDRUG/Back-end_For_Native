package com.example.demo.Services;

import com.example.demo.Repositorys.Repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;
}
