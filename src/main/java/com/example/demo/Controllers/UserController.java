package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.Card;
import com.example.demo.Repositorys.Entity.User;
import com.example.demo.Services.CardService;
import com.example.demo.Services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Пользователь")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        logger.info("Fetching users");
        return userService.getUsers();
    }

    // Получение пользователя по ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        logger.info("Fetching user by id={}", id);
        return userService.getUserById(id);
    }

    // Получение пользователя по имени
    @GetMapping("/name/{name}")
    public List<User> getUsersByName(@PathVariable String name) {
        logger.info("Fetching user by name={}", name);
        return userService.findByName(name);
    }

    // Получение пользователя по фамилии
    @GetMapping("/surname/{surname}")
    public List<User> getUsersBySurname(@PathVariable String surname) {
        logger.info("Fetching user by surname={}", surname);
        return userService.findBySurname(surname);
    }

    // Получение пользователя по электронной почте
    @GetMapping("/email/{email}")
    public List<User> getUsersByEmail(@PathVariable String email) {
        logger.info("Fetching user by email={}", email);
        return userService.findByEmail(email);
    }

    // Получение пользователя по возрасту
    @GetMapping("/age/{age}")
    public List<User> getUsersByAge(@PathVariable Integer age) {
        return userService.findByAge(age);
    }

    // Комбинированный поиск
    @GetMapping("/search")
    public List<User> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer age) {
        logger.info("Fetching user by any param without id: name={}, surname={}, email={}, age={}", name, surname, email, age);
        return userService.search(name, surname, email, age);
    }
}