package com.idus.homework.security.jwt;

public interface JwtProperties {
    String secretKey = "idus-homework-daeun-ryu";
    String tokenPrefix = "Bearer ";
    String accessHeader = "Authorization";
    String refreshHeader = "refreshToken";
    long accessTokenExpireTime = 60 * 60 * 1000;
    long refreshTokenExpireTime = 60 * 10000 * 1000;
    int refreshTokenReIssueDate = 3;
}
