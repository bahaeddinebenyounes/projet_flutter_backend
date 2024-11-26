package com.example.game.secutity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf().disable() // Disable CSRF for simplicity (not recommended in production without further consideration)
	            .authorizeRequests()
	            .requestMatchers("/api/users/register", "/api/users/login").permitAll() // Allow public access to register endpoint
	            .requestMatchers("/api/level-times/record", "/api/level-times/best/{userId}").permitAll() // Allow public access to level time record and best time endpoints
	            .anyRequest().authenticated() // Protect all other endpoints
	            .and()
	            .httpBasic(); // Enable basic authentication (optional)

	        return http.build();
	    }
}
