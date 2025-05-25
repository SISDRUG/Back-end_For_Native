package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.Card;
import com.example.demo.Repositorys.Entity.Credential;
import com.example.demo.Repositorys.Entity.Currency;
import com.example.demo.Repositorys.Entity.LoginDetail;
import com.example.demo.Repositorys.Entity.Operation;
import com.example.demo.Repositorys.Entity.Role;
import com.example.demo.Repositorys.Entity.User;
import com.example.demo.Repositorys.Repository.BankAccountsCredentialRepository;
import com.example.demo.Repositorys.Repository.CredentialRepository;
import com.example.demo.Repositorys.Repository.LoginDetailRepository;
import com.example.demo.Repositorys.Repository.RoleRepository;
import com.example.demo.Repositorys.Repository.UserRepository;
import com.example.demo.Services.CredentialService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/rest/admin-ui/credentials")
@RequiredArgsConstructor
public class CredentialResource {

    private final CredentialRepository credentialRepository;
    private final CredentialService credentialService;
    private final UserRepository userRepository;
    private final LoginDetailRepository loginDetailRepository;
    private final RoleRepository roleRepository;
    private final BankAccountsCredentialRepository bankAccountsCredentialRepository;

    private final ObjectMapper objectMapper;

    // DTO для входящих данных
    private static class CredentialCreateDTO {
        private Long userId;
        private Long loginDetailsId;
        private Long roleId;

        // Геттеры и сеттеры
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getLoginDetailsId() { return loginDetailsId; }
        public void setLoginDetailsId(Long loginDetailsId) { this.loginDetailsId = loginDetailsId; }
        public Long getRoleId() { return roleId; }
        public void setRoleId(Long roleId) { this.roleId = roleId; }
    }

    @GetMapping
    public PagedModel<Credential> getAll(@ParameterObject Pageable pageable) {
        Page<Credential> credentials = credentialRepository.findAll(pageable);
        return new PagedModel<>(credentials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Credential> getOne(@PathVariable Long id) {
        return credentialService.getCredentialById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Credential> getMany(@RequestParam List<Long> ids) {
        return credentialRepository.findAllById(ids);
    }

    @PostMapping
    public ResponseEntity<Credential> create(@RequestBody @Valid CredentialCreateDTO dto) throws Exception {
        Credential credential = new Credential();
        
        // Получаем пользователя
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new Exception("User not found for ID: " + dto.getUserId()));
        credential.setUser(user);
        
        // Получаем логин
        LoginDetail loginDetail = loginDetailRepository.findById(dto.getLoginDetailsId())
                .orElseThrow(() -> new Exception("LoginDetail not found for ID: " + dto.getLoginDetailsId()));
        credential.setEmail(loginDetail);
        
        // Получаем роль
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new Exception("Role not found for ID: " + dto.getRoleId()));
        credential.setRole(role);
        
        return ResponseEntity.ok(credentialRepository.save(credential));
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
    @Transactional
    public ResponseEntity<Credential> delete(@PathVariable Long id) {
        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credential not found"));
        
        // Удаляем связанные записи из bank_accounts_credentials
        bankAccountsCredentialRepository.deleteAll(
            bankAccountsCredentialRepository.findAllByUserId(credential.getUser().getId())
        );
        
        // Удаляем учетные данные
        credentialRepository.delete(credential);
        
        return ResponseEntity.ok(credential);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        credentialRepository.deleteAllById(ids);
    }

    @GetMapping("/login/{loginId}")
    public ResponseEntity<Credential> getByLoginId(@PathVariable Long loginId) {
        return credentialRepository.findByEmailId(loginId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credential not found for login ID: " + loginId));
    }
}
