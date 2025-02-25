package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.BankAccount;
import com.example.demo.Repositorys.Entity.Currency;
import com.example.demo.Repositorys.Entity.User;
import com.example.demo.Repositorys.Repository.UserRepository;
import com.example.demo.Services.UserService;
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
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/rest/admin-ui/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserRepository userRepository;
    private final UserService userService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<User> getAll(@ParameterObject Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return new PagedModel<>(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getOne(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/by-ids")
    public List<User> getMany(@RequestParam List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    /// TODO перенести в сервис
    @PostMapping
    public User create(@RequestBody @Valid JsonNode patchNode) throws IOException {
        User user = new User();
        objectMapper.readerForUpdating(user).readValue(patchNode);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        User user = userService.patchUser(id,patchNode);
        return ResponseEntity.ok(user);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<User> users = userRepository.findAllById(ids);

        for (User user : users) {
            objectMapper.readerForUpdating(user).readValue(patchNode);
            user.setUpdatedAt(Instant.now());
        }

        List<User> resultUsers = userRepository.saveAll(users);
        return resultUsers.stream()
                .map(User::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public User delete(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
        return user;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        userRepository.deleteAllById(ids);
    }
}
