package com.idus.homework.security.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final JwtProvider jwtProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            jwtProvider.validateHeader(request);
            String accessToken = jwtProvider.getHeaderAccessToken(request);
            String refreshToken = jwtProvider.getHeaderRefreshToken(request);

            if (jwtProvider.getRedisAccessToken(accessToken) != null ||
                    jwtProvider.getRedisRefreshToken(refreshToken) == null) {
                throw new ExpiredJwtException(null, null, null);
            }

            jwtProvider.validateToken(refreshToken);
            String username = jwtProvider.getUsername(refreshToken);
            refreshToken = jwtProvider.reIssueRefreshToken(refreshToken, username, response);

            try {
                jwtProvider.validateToken(accessToken);
            } catch (TokenExpiredException e) {
                accessToken = jwtProvider.createAccessToken(username);
                jwtProvider.setHeaderAccessToken(response, accessToken);
            }

            SecurityContextHolder.getContext().setAuthentication(jwtProvider.getAuthentication(refreshToken));
        } catch (ExpiredJwtException e) {
            request.setAttribute("JwtException", "토큰이 만료되었습니다. 다시 로그인 해주세요.");
        } catch (Exception e) {
            request.setAttribute("JwtException", "잘못된 토큰입니다.");
        }

        chain.doFilter(request, response);
    }
}
