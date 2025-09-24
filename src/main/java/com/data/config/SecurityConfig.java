package com.data.config;

import com.data.security.JwtAuthenticationFilter;
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

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/refresh").permitAll()
                        .requestMatchers("/api/news/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/parking-lots/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/parking-lots/**").hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/parking-lots/**").hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/parking-lots/**").hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").hasAnyRole("CLIENT", "ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/auth/update").hasAnyRole("CLIENT", "ADMIN", "OWNER")
                        .requestMatchers("/api/bookings/owner/**").hasRole("OWNER")
                        .requestMatchers("/api/owner/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/bookings/**").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/bookings/**").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/**").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/api/vehicles/me").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/vehicles").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/vehicles/**").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/vehicles/**").hasRole("CLIENT")
                        .requestMatchers("/api/vehicles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/payments/create").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/payments/callback").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/payments/booking/**").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/payments/*/refund").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payments").hasRole("ADMIN")
                        .requestMatchers("/api/owner/**").hasRole("OWNER")
                        .requestMatchers("/api/bookings/owner/**").hasRole("OWNER")
                        .requestMatchers("/api/auth/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/vehicle-entry/detect").permitAll()
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, e) -> res.sendError(401, "Unauthorized")))
                .cors(cors -> {});

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
