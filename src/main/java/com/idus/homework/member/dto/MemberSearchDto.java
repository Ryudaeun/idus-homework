package com.idus.homework.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class MemberSearchDto {
    @Schema(description = "이름(미 입력 시 전체 조회)", nullable = true)
    private String name = "";
    @Schema(description = "이메일(미 입력 시 전체 조회)", nullable = true)
    private String email = "";
}
