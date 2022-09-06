package com.idus.homework.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.idus.homework.common.ErrorCode;
import com.idus.homework.common.service.RedisService;
import com.idus.homework.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final CustomUserDetailsService customUserDetailService;
    private final RedisService redisService;
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(JwtProperties.secretKey.getBytes());
    }

    public String getUsername(String token) {
        return JWT.require(Algorithm.HMAC512(secretKey)).build()
                .verify(token)
                .getClaim("username").asString();
    }

    public UserDetails getUserDetails(String username) {
        return customUserDetailService.loadUserByUsername(username);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetails(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getHeaderAccessToken(HttpServletRequest request) {
        return request.getHeader(JwtProperties.accessHeader).replace(JwtProperties.tokenPrefix, "");
    }

    public String getHeaderRefreshToken(HttpServletRequest request) {
        return request.getHeader(JwtProperties.refreshHeader).replace(JwtProperties.tokenPrefix, "");
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(JwtProperties.accessHeader, JwtProperties.tokenPrefix + accessToken);
    }

    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader(JwtProperties.refreshHeader, JwtProperties.tokenPrefix + refreshToken);
    }

    public String createAccessToken(String username){
        return this.createToken(username, JwtProperties.accessTokenExpireTime);
    }

    public String createRefreshToken(String username) {
        return this.createToken(username, JwtProperties.refreshTokenExpireTime);
    }

    private String createToken(String username, long tokenValid) {
        Date now = new Date();
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + tokenValid))
                .withClaim("username", username)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public void reIssueRefreshToken(String refreshToken, String username, HttpServletResponse response) {
        if (checkNeedToReIssueRefreshToken(refreshToken)) {
            String newRefreshToken = createRefreshToken(username);
            setHeaderRefreshToken(response, newRefreshToken);
            setRedisRefreshToken(newRefreshToken, username);
        }
    }

    public void validateToken(String token) {
        try {
            getTokenExpiresAt(token);
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException(ErrorCode.EXPIRED_TOKEN.getMessage(), null);
        }
    }

    public void validateHeader(HttpServletRequest request) {
        if (request.getHeader(JwtProperties.accessHeader) == null) {
            throw new IllegalArgumentException(ErrorCode.ACCESS_TOKEN_NOT_EXISTS.getMessage());
        } else if (request.getHeader(JwtProperties.refreshHeader) == null) {
            throw new IllegalArgumentException(ErrorCode.REFRESH_TOKEN_NOT_EXISTS.getMessage());
        }
    }

    public String getRedisAccessToken(String accessToken) {
        return redisService.getValues(accessToken);
    }

    public String getRedisRefreshToken(String refreshToken) {
        return redisService.getValues(refreshToken);
    }

    public void setRedisAccessToken(String accessToken, String username, Long expireTime) {
        redisService.setValues(accessToken, username, expireTime);
    }

    public void setRedisRefreshToken(String refreshToken, String username) {
        redisService.setValues(refreshToken, username, JwtProperties.refreshTokenExpireTime);
    }

    public void removeRedisRefreshToken(String refreshToken) {
        redisService.deleteValues(refreshToken);
    }

    private boolean checkNeedToReIssueRefreshToken(String refreshToken) {
        try {
            Date expiresAt = getTokenExpiresAt(refreshToken);
            Date afterDays = Timestamp.valueOf(LocalDateTime.now().plusDays(JwtProperties.refreshTokenReIssueDate));

            if (expiresAt.before(afterDays)) {
                return true;
            }
        } catch (TokenExpiredException e) {
            return true;
        }
        return false;
    }

    private Date getTokenExpiresAt(String token) {
        return JWT.require(Algorithm.HMAC512(secretKey)).build()
                .verify(token)
                .getExpiresAt();
    }

    public void logout(HttpServletRequest request) {
        try {
            validateHeader(request);

            String accessToken = this.getHeaderAccessToken(request);
            String refreshToken = this.getHeaderRefreshToken(request);

            removeRedisRefreshToken(refreshToken);

            Long expiresAtTime = getTokenExpiresAt(accessToken).getTime();
            Long nowTime = new Date().getTime();
            setRedisAccessToken(accessToken, "logout", (expiresAtTime - nowTime));
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException(ErrorCode.EXPIRED_TOKEN.getMessage(), null);
        }
    }
}
