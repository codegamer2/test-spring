package com.moviereview.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class OAuth2Config {

    private final AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http
        //     .oauth2Login(oauth2 -> oauth2
        //         .successHandler(oAuth2AuthenticationSuccessHandler)
        //         .userInfoEndpoint(userInfo -> userInfo
        //             .userService(customOAuth2UserService)
        //         )
        //     )
        //     .authorizeHttpRequests(auth -> auth
        //         .requestMatchers("/", "/login", "/register", "/api/auth/**").permitAll()
        //         .anyRequest().authenticated()
        //     );

        return http.build();
    }
} 