package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.Role;
import com.example.demo.Repositorys.Repository.RoleRepository;
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
@RequestMapping("/rest/admin-ui/roles")
@RequiredArgsConstructor
public class RoleResource {

    private final RoleRepository roleRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Role> getAll(@ParameterObject Pageable pageable) {
        Page<Role> roles = roleRepository.findAll(pageable);
        return new PagedModel<>(roles);
    }

    @GetMapping("/{id}")
    public Role getOne(@PathVariable Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        return roleOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Role> getMany(@RequestParam List<Long> ids) {
        return roleRepository.findAllById(ids);
    }

    @PostMapping
    public Role create(@RequestBody @Valid Role role) {
        return roleRepository.save(role);
    }

    @PatchMapping("/{id}")
    public Role patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Role role = roleRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(role).readValue(patchNode);

        return roleRepository.save(role);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Role> roles = roleRepository.findAllById(ids);

        for (Role role : roles) {
            objectMapper.readerForUpdating(role).readValue(patchNode);
        }

        List<Role> resultRoles = roleRepository.saveAll(roles);
        return resultRoles.stream()
                .map(Role::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Role delete(@PathVariable Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null) {
            roleRepository.delete(role);
        }
        return role;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        roleRepository.deleteAllById(ids);
    }
}
