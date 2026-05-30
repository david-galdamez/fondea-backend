package com.project.fondea.security;

import com.project.fondea.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ── Autenticación ────────────────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        // ── Admin ────────────────────────────────────────────────
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/locations/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/locations/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/locations/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        // ── Creador — rutas específicas ANTES del wildcard público
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/mine").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/drafts").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/api/campaigns").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/api/campaigns/*/submit").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/api/campaigns/*/updates").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/*/faqs/manage").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.PUT, "/api/campaigns/*/faqs").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/campaigns/*/faqs").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/api/campaigns/*/rewards").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/*/rewards/manage").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/campaigns/*/rewards/*").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/api/withdrawals").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.GET, "/api/withdrawals/mine").hasRole("CREATOR")
                        // ── Sponsor ──────────────────────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/pledges").hasRole("SPONSOR")
                        .requestMatchers(HttpMethod.GET, "/api/pledges/mine").hasRole("SPONSOR")
                        .requestMatchers(HttpMethod.GET, "/api/certificates/**").hasRole("SPONSOR")

                        // ── Públicos — wildcards al final ────────────────────────
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/locations/**").permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
