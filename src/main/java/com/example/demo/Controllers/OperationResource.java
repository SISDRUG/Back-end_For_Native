package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.Operation;
import com.example.demo.Repositorys.Repository.OperationRepository;
import com.example.demo.Services.OperationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/rest/admin-ui/operations")
@RequiredArgsConstructor
public class OperationResource {

    private final OperationRepository operationRepository;

    private final ObjectMapper objectMapper;

    private final OperationService operationService;

    @GetMapping
    public PagedModel<Operation> getAll(@ParameterObject Pageable pageable) {
        Page<Operation> operations = operationRepository.findAll(pageable);
        return new PagedModel<>(operations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operation> getOne(@PathVariable Long id) {
        Operation operation = operationService.getOperationById(id);
        return ResponseEntity.ok(operation);

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
    public ResponseEntity<Operation> patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Operation operation = operationService.patchOperation(id,patchNode);
        return ResponseEntity.ok(operation);
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
