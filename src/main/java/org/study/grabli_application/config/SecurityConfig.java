package org.study.grabli_application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.study.grabli_application.security.AuthenticationFailureHandlerImpl;
import org.study.grabli_application.security.AuthenticationSuccessHandlerImpl;
import org.study.grabli_application.security.LogoutSuccessHandlerImpl;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                // .csrf(customizer -> customizer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(customizer -> customizer
                        // .requestMatchers("/street-objects").authenticated()
                        .requestMatchers("/**").permitAll()
                )
                .formLogin(customizer -> customizer
                        .loginPage("/login")
                        .successHandler(new AuthenticationSuccessHandlerImpl())
                        .failureHandler(new AuthenticationFailureHandlerImpl())
                )
                .exceptionHandling(customizer -> customizer
                        .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                )
                .logout(customizer -> customizer
                        .logoutSuccessHandler(new LogoutSuccessHandlerImpl())
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
