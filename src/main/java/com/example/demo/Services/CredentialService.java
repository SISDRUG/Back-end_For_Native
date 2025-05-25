package com.example.demo.Services;

import com.example.demo.Repositorys.Entity.Credential;
import com.example.demo.Repositorys.Entity.LoginDetail;
import com.example.demo.Repositorys.Entity.Role;
import com.example.demo.Repositorys.Repository.CredentialRepository;
import com.example.demo.Repositorys.Repository.LoginDetailRepository;
import com.example.demo.Repositorys.Repository.RoleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CredentialService {

    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;
    private final LoginDetailRepository loginDetailRepository;

    public CredentialService(CredentialRepository credentialRepository, RoleRepository roleRepository, LoginDetailRepository loginDetailRepository) {
        this.credentialRepository = credentialRepository;
        this.roleRepository = roleRepository;
        this.loginDetailRepository = loginDetailRepository;
    }

    public Optional<Credential> getCredentialById(Long id) {
        return credentialRepository.findById(id);
    }

    @Transactional
    public Credential patchCredential(Long id, JsonNode patchNode) {
        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credential not found"));

        if (patchNode.has("login_details_id")) {
            Long loginDetailsId = patchNode.get("login_details_id").asLong();
            LoginDetail loginDetail = loginDetailRepository.findById(loginDetailsId)
                    .orElseThrow(() -> new RuntimeException("LoginDetail not found"));
            credential.setEmail(loginDetail);
        }

        if (patchNode.has("role_id")) {
            Long roleId = patchNode.get("role_id").asLong();
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            credential.setRole(role);
        }

        return credentialRepository.save(credential);
    }
}
