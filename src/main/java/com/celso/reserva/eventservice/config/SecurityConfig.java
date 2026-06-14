package com.celso.reserva.eventservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Optional;

/**
 * Configuracao de seguranca do EventService.
 * Todos os endpoints exigem autenticacao (token JWT valido).
 *
 * Em 'dev': JwtAuthenticationFilter valida token Bearer.
 * Em 'prod': GatewayHeaderAuthenticationFilter le headers injetados pelo gateway.
 * Actuator e liberado para health checks e metricas.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final Optional<JwtAuthenticationFilter> jwtFilter;
    private final Optional<GatewayHeaderAuthenticationFilter> gatewayFilter;

    public SecurityConfig(Optional<JwtAuthenticationFilter> jwtFilter,
                          Optional<GatewayHeaderAuthenticationFilter> gatewayFilter) {
        this.jwtFilter = jwtFilter;
        this.gatewayFilter = gatewayFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                );

        jwtFilter.ifPresent(filter ->
                http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class));

        gatewayFilter.ifPresent(filter ->
                http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class));

        return http.build();
    }
}