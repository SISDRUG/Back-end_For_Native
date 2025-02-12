package com.example.demo.Services;

import lombok.RequiredArgsConstructor;
import org.apache.http.auth.Credentials;
import org.springframework.stereotype.Service;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class CredentialsService implements Credentials {
    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getPassword() {
        return "";
    }
}
