package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.Operation;
import com.example.demo.Repositorys.Repository.OperationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin-ui/operations")
@RequiredArgsConstructor
public class OperationResource {

    private final OperationRepository operationRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Operation> getAll(@ParameterObject Pageable pageable) {
        Page<Operation> operations = operationRepository.findAll(pageable);
        return new PagedModel<>(operations);
    }

    @GetMapping("/{id}")
    public Operation getOne(@PathVariable Long id) {
        Optional<Operation> operationOptional = operationRepository.findById(id);
        return operationOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Operation> getMany(@RequestParam List<Long> ids) {
        return operationRepository.findAllById(ids);
    }

    @PostMapping
    public Operation create(@RequestBody @Valid Operation operation) {
        return operationRepository.save(operation);
    }

    @PatchMapping("/{id}")
    public Operation patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Operation operation = operationRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(operation).readValue(patchNode);

        return operationRepository.save(operation);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Operation> operations = operationRepository.findAllById(ids);

        for (Operation operation : operations) {
            objectMapper.readerForUpdating(operation).readValue(patchNode);
        }

        List<Operation> resultOperations = operationRepository.saveAll(operations);
        return resultOperations.stream()
                .map(Operation::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Operation delete(@PathVariable Long id) {
        Operation operation = operationRepository.findById(id).orElse(null);
        if (operation != null) {
            operationRepository.delete(operation);
        }
        return operation;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        operationRepository.deleteAllById(ids);
    }
}
