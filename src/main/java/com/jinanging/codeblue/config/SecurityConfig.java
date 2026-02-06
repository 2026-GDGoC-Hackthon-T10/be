package com.jinanging.codeblue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 입구에서 수상한 요청으로 오해하지 않게 CSRF 끄기
                .csrf(AbstractHttpConfigurer::disable)

                // 2. WebConfig 설정과 연결
                .cors(cors -> {})

                // 3. 수문장에게 "이 사람들은 그냥 통과시켜줘"라고 명령
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/api-docs/**",      // application.yml에 설정한 경로
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()            // Swagger 관련은 무조건 통과
                        .requestMatchers("/api/**").permitAll() // 우리 게임 API도 무조건 통과
                        .anyRequest().permitAll()               // 그 외 모든 요청도 일단 허용
                );

        return http.build();
    }
}