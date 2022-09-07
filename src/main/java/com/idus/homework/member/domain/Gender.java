package com.idus.homework.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    F("여성"),
    M("남성");

    private final String name;

    @JsonCreator
    public static Gender from(String gender) {
        return Gender.valueOf(gender.toUpperCase());
    }
}
