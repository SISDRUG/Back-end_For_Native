package com.example.demo.Services;

import com.example.demo.Repositorys.Entity.Credential;
import com.example.demo.Repositorys.Repository.CredentialRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class CredentialService {
    private final CredentialRepository credentialRepository;
    private final ObjectMapper objectMapper;

    public Credential getCredentialById(Long id) {
        return credentialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public Credential patchCredential (Long id, JsonNode patchNode) throws IOException {
        Credential credential = credentialRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
        objectMapper.readerForUpdating(credential).readValue(patchNode);
        return credentialRepository.save(credential);
    }
}
