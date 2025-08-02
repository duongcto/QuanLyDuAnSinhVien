package com.example.quanlyxuong.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 👈 THÊM DÒNG NÀY
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // Cho phép truy cập tự do tất cả API REST
                        .requestMatchers("/homeDangNhap","/dangNhap","/dangNhapQuanLy","/hien-thi-co-so","/hienThiSinhVien",
                                "/danhSachNhanVien","/css/**", "/js/**", "/image/**","/admin/boMon/**").permitAll() // cho phép truy cập tự do
                        .anyRequest().authenticated() // các URL khác yêu cầu đăng nhập
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable()) // 👈 Dòng này tắt X-Frame-Options
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/homeDangNhap") // khi cần login thì hiển thị trang /home chứ không chuyển sang Google ngay
                        .defaultSuccessUrl("/singinGoogle", true) // sau khi login thành công thì chuyển đến đây
                );

        return http.build();
    }
}
