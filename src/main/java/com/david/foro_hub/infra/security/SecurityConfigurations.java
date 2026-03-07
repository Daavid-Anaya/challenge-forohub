package com.david.foro_hub.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity htpp) throws Exception {
        return htpp.csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(request -> {
                
            // Públicos — sin autenticación
            request.requestMatchers(HttpMethod.POST, "/auth").permitAll();
            request.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();

            // Perfiles — solo ADMIN
            request.requestMatchers(HttpMethod.POST, "/perfiles").hasRole("ADMIN");
            request.requestMatchers(HttpMethod.PUT, "/perfiles/**").hasRole("ADMIN");
            request.requestMatchers(HttpMethod.DELETE, "/perfiles/**").hasRole("ADMIN");
            request.requestMatchers(HttpMethod.GET, "/perfiles/**").hasAnyRole("ADMIN", "MODERADOR");
            request.requestMatchers(HttpMethod.GET, "/perfiles").hasAnyRole("ADMIN", "MODERADOR");

            // Cursos — ADMIN gestiona, USER y MODERADOR consultan
            request.requestMatchers(HttpMethod.POST, "/cursos").hasRole("ADMIN");
            request.requestMatchers(HttpMethod.PUT, "/cursos/**").hasRole("ADMIN");
            request.requestMatchers(HttpMethod.DELETE, "/cursos/**").hasRole("ADMIN");
            request.requestMatchers(HttpMethod.GET, "/cursos/**").hasAnyRole("ADMIN", "USER", "MODERADOR");
            request.requestMatchers(HttpMethod.GET, "/cursos").hasAnyRole("ADMIN", "USER", "MODERADOR");

            // Usuarios — ADMIN gestiona, MODERADOR consulta
            request.requestMatchers(HttpMethod.GET, "/usuarios/**").hasAnyRole("ADMIN", "MODERADOR");
            request.requestMatchers(HttpMethod.GET, "/usuarios").hasAnyRole("ADMIN", "MODERADOR");
            request.requestMatchers(HttpMethod.PUT, "/usuarios/**").hasAnyRole("ADMIN", "USER");
            request.requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasRole("ADMIN");

            // Tópicos — todos autenticados pueden crear y consultar, MODERADOR y ADMIN gestionan
            request.requestMatchers(HttpMethod.POST, "/topicos").hasAnyRole("ADMIN", "USER", "MODERADOR");
            request.requestMatchers(HttpMethod.GET, "/topicos/**").hasAnyRole("ADMIN", "USER", "MODERADOR");
            request.requestMatchers(HttpMethod.GET, "/topicos").hasAnyRole("ADMIN", "USER", "MODERADOR");
            request.requestMatchers(HttpMethod.PUT, "/topicos/**").hasAnyRole("ADMIN", "MODERADOR");
            request.requestMatchers(HttpMethod.DELETE, "/topicos/**").hasRole("ADMIN");

            // Respuestas — todos autenticados pueden crear y consultar, MODERADOR y ADMIN gestionan
            request.requestMatchers(HttpMethod.POST, "/respuestas").hasAnyRole("ADMIN", "USER", "MODERADOR");
            request.requestMatchers(HttpMethod.GET, "/respuestas/**").hasAnyRole("ADMIN", "USER", "MODERADOR");
            request.requestMatchers(HttpMethod.GET, "/respuestas").hasAnyRole("ADMIN", "USER", "MODERADOR");
            request.requestMatchers(HttpMethod.PUT, "/respuestas/**").hasAnyRole("ADMIN", "MODERADOR", "USER");
            request.requestMatchers(HttpMethod.DELETE, "/respuestas/**").hasRole("ADMIN");

            request.anyRequest().authenticated();
            })
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}