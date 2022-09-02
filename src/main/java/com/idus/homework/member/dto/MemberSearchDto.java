package com.idus.homework.member.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class MemberSearchDto {
    private String name = "";
    private String email = "";
}
