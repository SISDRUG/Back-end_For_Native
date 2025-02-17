package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.Credential;
import com.example.demo.Repositorys.Repository.CredentialRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin-ui/credentials")
@RequiredArgsConstructor
public class CredentialResource {

    private final CredentialRepository credentialRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Credential> getAll(@ParameterObject Pageable pageable) {
        Page<Credential> credentials = credentialRepository.findAll(pageable);
        return new PagedModel<>(credentials);
    }

    @GetMapping("/{id}")
    public Credential getOne(@PathVariable Long id) {
        Optional<Credential> credentialOptional = credentialRepository.findById(id);
        return credentialOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Credential> getMany(@RequestParam List<Long> ids) {
        return credentialRepository.findAllById(ids);
    }

    @PostMapping
    public Credential create(@RequestBody @Valid Credential credential) {
        return credentialRepository.save(credential);
    }

    @PatchMapping("/{id}")
    public Credential patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Credential credential = credentialRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(credential).readValue(patchNode);

        return credentialRepository.save(credential);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Credential> credentials = credentialRepository.findAllById(ids);

        for (Credential credential : credentials) {
            objectMapper.readerForUpdating(credential).readValue(patchNode);
        }

        List<Credential> resultCredentials = credentialRepository.saveAll(credentials);
        return resultCredentials.stream()
                .map(Credential::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Credential delete(@PathVariable Long id) {
        Credential credential = credentialRepository.findById(id).orElse(null);
        if (credential != null) {
            credentialRepository.delete(credential);
        }
        return credential;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        credentialRepository.deleteAllById(ids);
    }
}
