package com.example.demo.Controllers;
import com.example.demo.Repositorys.Entity.User;
import com.example.demo.Repositorys.Repository.UserRepository;
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
@RequestMapping("/rest/admin-ui/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<User> getAll(@ParameterObject Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return new PagedModel<>(users);
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<User> getMany(@RequestParam List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userRepository.save(user);
    }

    @PatchMapping("/{id}")
    public User patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(user).readValue(patchNode);

        return userRepository.save(user);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<User> users = userRepository.findAllById(ids);

        for (User user : users) {
            objectMapper.readerForUpdating(user).readValue(patchNode);
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
