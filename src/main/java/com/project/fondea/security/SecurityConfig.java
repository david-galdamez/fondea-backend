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
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Públicos: registro, login, ver campañas
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/*/faqs").permitAll()

                        // Solo ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Solo CREATOR
                        .requestMatchers(HttpMethod.POST, "/api/campaigns").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/api/campaigns/*/updates").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/api/withdrawals").hasRole("CREATOR")

                        // Solo SPONSOR
                        .requestMatchers(HttpMethod.POST, "/api/pledges").hasRole("SPONSOR")
                        .requestMatchers(HttpMethod.GET, "/api/certificates/**").hasRole("SPONSOR")

                        // Todo lo demás requiere estar autenticado
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
