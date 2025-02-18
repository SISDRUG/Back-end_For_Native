package com.example.demo.Services;

import com.example.demo.Repositorys.Entity.Operation;
import com.example.demo.Repositorys.Repository.OperationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class OperationService {
    private final OperationRepository operationRepository;
    private final ObjectMapper objectMapper;

    public Operation getOperationById(Long id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public Operation patchOperation (Long id, JsonNode patchNode) throws IOException {
        Operation operation = operationRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
        objectMapper.readerForUpdating(operation).readValue(patchNode);
        return operationRepository.save(operation);
    }
}
