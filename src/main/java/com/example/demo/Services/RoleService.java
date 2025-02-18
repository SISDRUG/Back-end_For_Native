package com.example.demo.Services;

import com.example.demo.Repositorys.Entity.Role;
import com.example.demo.Repositorys.Repository.RoleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public Role patchRole (Long id, JsonNode patchNode) throws IOException {
        Role role = roleRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
        objectMapper.readerForUpdating(role).readValue(patchNode);
        return roleRepository.save(role);
    }
}
