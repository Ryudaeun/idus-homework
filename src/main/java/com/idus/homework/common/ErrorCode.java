package com.idus.homework.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    MEMBER_NOT_EXISTS("사용자를 찾을 수 없습니다."),
    ALREADY_EXISTS_USERNAME("이미 사용 중인 아이디입니다.");

    private final String message;
}
