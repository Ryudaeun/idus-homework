package com.idus.homework.security.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.idus.homework.common.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final List<String> excludeUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            jwtProvider.validateHeader(request);
            String accessToken = jwtProvider.getHeaderAccessToken(request);
            String refreshToken = jwtProvider.getHeaderRefreshToken(request);

            if (jwtProvider.getRedisAccessToken(accessToken) != null ||
                    jwtProvider.getRedisRefreshToken(refreshToken) == null) {
                throw new TokenExpiredException(ErrorCode.EXPIRED_TOKEN.getMessage(), null);
            }

            jwtProvider.validateToken(refreshToken);
            String username = jwtProvider.getUsername(refreshToken);
            jwtProvider.reIssueRefreshToken(refreshToken, username, response);

            try {
                jwtProvider.validateToken(accessToken);
            } catch (TokenExpiredException e) {
                accessToken = jwtProvider.createAccessToken(username);
                jwtProvider.setHeaderAccessToken(response, accessToken);
            }

            SecurityContextHolder.getContext().setAuthentication(jwtProvider.getAuthentication(accessToken));
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            request.setAttribute("exception", ErrorCode.INVALID_TOKEN.name());
        } catch (ExpiredJwtException | TokenExpiredException e) {
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.name());
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN.name());
        } catch (Exception e) {
            e.printStackTrace();
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return excludeUrl.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
}
