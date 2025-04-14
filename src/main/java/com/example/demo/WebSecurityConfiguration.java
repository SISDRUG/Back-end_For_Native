package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers("/rest/user-app").hasAnyRole("ADMIN", "SUPERADMIN", "USER")
                .requestMatchers("/rest/admin-ui/", "/rest/admin-ui").hasAnyRole("ADMIN", "SUPERADMIN")
                .requestMatchers("/*").hasRole("anonymous")
                .anyRequest().authenticated());
        http.headers(Customizer.withDefaults());
        http.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                .jwt(jwt -> jwt
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())));
//        http.anonymous(Customizer.withDefaults());
        http.csrf(Customizer.withDefaults());
        return http.build();
    }

    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}