package com.example.demo.Services;


import com.example.demo.Repositorys.Entity.User;
import com.example.demo.Repositorys.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void someMethod() {
        logger.info("This is an info message");
        logger.warn("This is a warning message");
        logger.error("This is an error message");
    }

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null); // возвращает null, если пользователь не найден
    }

    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public List<User> findBySurname(String surname) {
        return userRepository.findBySurname(surname);
    }

    public List<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findByAge(Integer age) {
        return userRepository.findByAge(age);
    }

    public List<User> search(String name, String surname, String email, Integer age) {
        return userRepository.search(name, surname, email, age);
    }
}
