package com.example.demo.Repositorys.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "login_details", indexes = {
        @Index(name = "email", columnList = "email", unique = true)
})
public class LoginDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "last_login_date")
    private LocalDate lastLoginDate;

    @Column(name = "login_attempts")
    private Long loginAttempts;

    @Column(name = "user_status")
    private Long userStatus;

    @Column(name = "password_hash_salt")
    private Long passwordHashSalt;

    @Column(name = "two_factor_auth_enabled")
    private Boolean twoFactorAuthEnabled;

}