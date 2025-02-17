package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.Credential;
import com.example.demo.Repositorys.Repository.CredentialRepository;
import com.example.demo.Services.CredentialService;
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
@RequestMapping("/rest/admin-ui/credentials")
@RequiredArgsConstructor
public class CredentialResource {

    private final CredentialRepository credentialRepository;
    private final CredentialService credentialService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Credential> getAll(@ParameterObject Pageable pageable) {
        Page<Credential> credentials = credentialRepository.findAll(pageable);
        return new PagedModel<>(credentials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Credential> getOne(@PathVariable Long id) {
        Credential credential = credentialService.getCredentialById(id);
        return ResponseEntity.ok(credential);
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
    public ResponseEntity<Credential> patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Credential credential = credentialService.patchCredential(id,patchNode);
        return ResponseEntity.ok(credential);
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
