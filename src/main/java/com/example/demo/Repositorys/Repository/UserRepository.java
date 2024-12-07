package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository <User, Long> {

    List<User> findByName(String name);

    List<User> findBySurname(String surname);

    List<User> findByEmail(String email);

    List<User> findByAge(Integer age);

    // Реализация метода для комбинированного поиска
    @Query("SELECT u FROM User u WHERE (:name IS NULL OR u.name = :name) " +
            "AND (:surname IS NULL OR u.surname = :surname) " +
            "AND (:email IS NULL OR u.email = :email) " +
            "AND (:age IS NULL OR u.age = :age)")
    List<User> search(@Param("name") String name,
                      @Param("surname") String surname,
                      @Param("email") String email,
                      @Param("age") Integer age);
}
