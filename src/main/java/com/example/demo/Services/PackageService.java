package com.example.demo.Services;

import com.example.demo.Repositorys.Entity.Package;
import com.example.demo.Repositorys.Repository.PackageRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final ObjectMapper objectMapper;

    public Package getPackageById(Long id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public Package patchPackage (Long id, JsonNode patchNode) throws IOException {
        Package _package = packageRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
        objectMapper.readerForUpdating(_package).readValue(patchNode);
        return packageRepository.save(_package);
    }
}
