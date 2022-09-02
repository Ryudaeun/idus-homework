package com.idus.homework.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@RequiredArgsConstructor
public class Members {
    private final Page<Member> page;
}
