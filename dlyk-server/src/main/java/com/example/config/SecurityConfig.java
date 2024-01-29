package com.example.config;

import com.example.config.filter.TokenVerifyFilter;
import com.example.config.handler.MyAuthenticationFailureHandler;
import com.example.config.handler.MyAuthenticationSuccessHandler;
import com.example.config.handler.MyLogoutSuccessHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Resource
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Resource
    private TokenVerifyFilter tokenVerifyFilter;

    @Bean   //There is no PasswordEncoder mapped for the id "null"
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CorsConfigurationSource configurationSource) throws Exception {
        return httpSecurity
                .formLogin((formLogin) -> {
                    formLogin.loginProcessingUrl("/api/login")  //登录处理地址, 不需要Controller
                            .usernameParameter("loginAct")
                            .passwordParameter("loginPwd")
                            .successHandler(myAuthenticationSuccessHandler)
                            .failureHandler(myAuthenticationFailureHandler);
                })
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers("/api/login").permitAll()
                            .anyRequest().authenticated(); //其他任何请求都需要登录后才能访问
                })

                .csrf(AbstractHttpConfigurer::disable)  //禁用夸张请求伪造

                //支持跨域请求
                .cors((cors) -> {
                    cors.configurationSource(configurationSource);
                })

                .sessionManagement((session) -> {
                    //session创建策略
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                .addFilterBefore(tokenVerifyFilter, LogoutFilter.class)

                .logout((logout) -> {
                    logout.logoutUrl("/api/logout")
                            .logoutSuccessHandler(myLogoutSuccessHandler);
                })

                .build();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
