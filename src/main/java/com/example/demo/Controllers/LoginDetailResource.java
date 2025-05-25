package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.LoginDetail;
import com.example.demo.Repositorys.Repository.LoginDetailRepository;
import com.example.demo.Repositorys.Repository.CredentialRepository;
import com.example.demo.Repositorys.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin-ui/loginDetails")
@RequiredArgsConstructor
public class LoginDetailResource {

    private final LoginDetailRepository loginDetailRepository;
    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<LoginDetail> getAll(@ParameterObject Pageable pageable) {
        Page<LoginDetail> loginDetails = loginDetailRepository.findAll(pageable);
        return new PagedModel<>(loginDetails);
    }

    @GetMapping("/{id}")
    public LoginDetail getOne(@PathVariable Long id) {
        Optional<LoginDetail> loginDetailOptional = loginDetailRepository.findById(id);
        return loginDetailOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<LoginDetail> getMany(@RequestParam List<Long> ids) {
        return loginDetailRepository.findAllById(ids);
    }

    @PostMapping
    public LoginDetail create(@RequestBody @Valid LoginDetail loginDetail) {
        // Устанавливаем значения по умолчанию
        loginDetail.setLastLoginDate(java.time.LocalDate.now());
        loginDetail.setLoginAttempts(0L);
        loginDetail.setUserStatus(1L); // 1 - активный пользователь
        loginDetail.setPasswordHashSalt(0L); // По умолчанию без соли
        loginDetail.setTwoFactorAuthEnabled(false); // 2FA выключен по умолчанию
        
        return loginDetailRepository.save(loginDetail);
    }

    @PatchMapping("/{id}")
    public LoginDetail patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        LoginDetail loginDetail = loginDetailRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(loginDetail).readValue(patchNode);

        return loginDetailRepository.save(loginDetail);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<LoginDetail> loginDetails = loginDetailRepository.findAllById(ids);

        for (LoginDetail loginDetail : loginDetails) {
            objectMapper.readerForUpdating(loginDetail).readValue(patchNode);
        }

        List<LoginDetail> resultLoginDetails = loginDetailRepository.saveAll(loginDetails);
        return resultLoginDetails.stream()
                .map(LoginDetail::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<LoginDetail> delete(@PathVariable Long id) {
        LoginDetail loginDetail = loginDetailRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LoginDetail not found"));
        
        // Получаем ID пользователя перед удалением
        Long userId = loginDetail.getUser() != null ? loginDetail.getUser().getId() : null;
        
        // Удаляем логин
        loginDetailRepository.delete(loginDetail);
        
        // Если был связанный пользователь, удаляем его креды и самого пользователя
        if (userId != null) {
            System.out.println("Удаляем креды пользователя: " + userId);
            credentialRepository.deleteByUserId(userId);
            
            System.out.println("Удаляем пользователя: " + userId);
            userRepository.deleteById(userId);
        }
        
        return ResponseEntity.ok(loginDetail);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        loginDetailRepository.deleteAllById(ids);
    }
}
