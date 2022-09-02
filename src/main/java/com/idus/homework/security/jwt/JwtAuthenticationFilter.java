package com.idus.homework.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idus.homework.member.presentation.MemberDto;
import com.idus.homework.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            MemberDto.LoginRequest login =
                    new ObjectMapper().readValue(request.getInputStream(), MemberDto.LoginRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword(), null)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String username = userDetails.getUsername();

        String accessToken = jwtProvider.createAccessToken(username);
        String refreshToken = jwtProvider.createRefreshToken(username);

        jwtProvider.setRedisRefreshToken(refreshToken, username);
        jwtProvider.setHeaderAccessToken(response, accessToken);
        jwtProvider.setHeaderRefreshToken(response, refreshToken);
    }
}
