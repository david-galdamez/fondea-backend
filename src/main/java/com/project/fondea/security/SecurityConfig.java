package com.project.fondea.security;

import com.project.fondea.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ── Autenticación ────────────────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/featured").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()
                        // ── Admin ────────────────────────────────────────────────
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/locations/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/locations/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/locations/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        // ── Creador — rutas específicas ANTES del wildcard público
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/mine").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/drafts").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/api/campaigns").hasAnyRole("CREATOR", "SPONSOR")
                        .requestMatchers(HttpMethod.POST, "/api/campaigns/*/submit").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/api/campaigns/*/updates").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/*/faqs/manage").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.PUT, "/api/campaigns/*/faqs").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/campaigns/*/faqs").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/api/campaigns/*/rewards").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/*/pledges").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/*/rewards/manage").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/campaigns/*/rewards/*").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/api/withdrawals").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.GET, "/api/withdrawals/mine").hasRole("CREATOR")
                        // ── Sponsor ──────────────────────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/pledges").hasRole("SPONSOR")
                        .requestMatchers(HttpMethod.GET, "/api/pledges/mine").hasRole("SPONSOR")
                        .requestMatchers(HttpMethod.GET, "/api/certificates/**").hasRole("SPONSOR")
                        .requestMatchers(HttpMethod.POST, "/api/fraud-reports").hasRole("SPONSOR")
                        // Notifications
                        .requestMatchers(HttpMethod.GET, "/api/notifications/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/notifications/**").authenticated()
                        // ── Públicos — wildcards al final ────────────────────────
                        .requestMatchers(HttpMethod.GET, "/api/campaigns/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/locations/**").permitAll()
                        // Exportacion csv
                        .requestMatchers(HttpMethod.GET, "/api/export/campaigns/csv").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/export/campaigns/google-sheets").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();

        // Orígenes permitidos — configurables vía CORS_ALLOWED_ORIGINS
        configuration.setAllowedOrigins(Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList());

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Headers permitidos en el request
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept"
        ));

        // Permite enviar cookies y el header Authorization
        configuration.setAllowCredentials(true);

        // Cuánto tiempo el browser cachea la respuesta del preflight (en segundos)
        configuration.setMaxAge(3600L);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
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
