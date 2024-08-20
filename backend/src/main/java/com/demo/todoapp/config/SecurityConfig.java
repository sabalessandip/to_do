package com.demo.todoapp.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // httpSecurity.authorizeHttpRequests(auth -> {
        //     auth.requestMatchers("/health", "/info").permitAll()
        //     .anyRequest().authenticated();
        // });

        // httpSecurity.httpBasic(Customizer.withDefaults());

        // httpSecurity.csrf(Customizer.withDefaults());
        
        httpSecurity
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/health", "/info").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic()
            .and()
            .csrf().disable();

        return httpSecurity.build();
    }
}
