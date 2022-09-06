package com.idus.homework.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    UNKNOWN_ERROR("알 수 없는 오류가 발생하였습니다."),
    CHECK_LOGIN_INFO("아이디 또는 비밀번호를 다시 확인해주세요."),
    MEMBER_NOT_EXISTS("사용자를 찾을 수 없습니다."),
    ALREADY_EXISTS_USERNAME("이미 사용 중인 아이디입니다."),
    ACCESS_DENIED("접근 권한이 없습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("만료된 토큰입니다."),
    UNSUPPORTED_TOKEN("지원하지 않는 형식의 토큰입니다."),
    ACCESS_TOKEN_NOT_EXISTS("Access Token 정보를 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_EXISTS("Refresh Token 정보를 찾을 수 없습니다.");

    private final String message;
}
