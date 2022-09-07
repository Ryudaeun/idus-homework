package com.idus.homework.security.config;

import com.idus.homework.security.handler.CustomAccessDeniedHandler;
import com.idus.homework.security.handler.CustomAuthenticationEntryPoint;
import com.idus.homework.security.jwt.JwtAuthenticationFilter;
import com.idus.homework.security.jwt.JwtAuthorizationFilter;
import com.idus.homework.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private static final List<String> excludeUrl =
            List.of("/sign-up", "/login",
                    "/swagger*/**", "/v3/api-docs/**", "/configuration/**", "/webjars/**");

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeRequests(config -> config
                        .antMatchers(excludeUrl.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )
                .apply(new AuthorizationFilter())
                .and()
                .build();
    }

    public class AuthorizationFilter extends AbstractHttpConfigurer<AuthorizationFilter, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilterBefore(new JwtAuthorizationFilter(jwtProvider, excludeUrl), BasicAuthenticationFilter.class)
                    .addFilterBefore(new JwtAuthenticationFilter(authenticationManager, jwtProvider),
                            UsernamePasswordAuthenticationFilter.class);
        }
    }
}
