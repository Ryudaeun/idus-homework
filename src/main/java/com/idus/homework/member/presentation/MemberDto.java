package com.idus.homework.member.presentation;

import com.idus.homework.member.domain.Gender;
import com.idus.homework.member.domain.Member;
import com.idus.homework.member.domain.Members;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MemberDto {
    @Getter
    @RequiredArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "아이디를 입력해주세요.")
        private String username;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Getter
    @RequiredArgsConstructor
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class MemberRequest {
        @NotBlank(message = "아이디를 입력해주세요.")
        @Size(min = 5, max = 30, message = "아이디는 5~30자 내외로 입력해주세요.")
        private String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[~!@#$%^&*()-_=+|]?)[A-Za-z[0-9]~!@#$%^&*()-_=+|]{10,}$",
                message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함하여 최소 10자 이상 작성해주세요."
        )
        private String password;

        @NotBlank(message = "이름을 입력해주세요.")
        @Size(max = 20, message = "이름은 최대 20자까지 입력 가능합니다.")
        private String name;

        @NotBlank(message = "별명을 입력해주세요.")
        @Size(max = 30, message = "별명은 최대 30자까지 입력 가능합니다.")
        private String nickname;

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "^[0-9]*$", message = "전화번호는 숫자만 입력해주세요.")
        private String phone;

        @Email(message = "잘못된 이메일 형식입니다.")
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        private Gender gender;

        public Member toDomain() {
            return Member.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .nickname(nickname)
                    .phone(phone)
                    .email(email)
                    .gender(gender)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class MemberResponse {
        private Long id;
        private String username;
        private String name;
        private String nickname;
        private String phone;
        private String email;
        private String gender;
        private String orderNo;

        public static MemberResponse from(Member member) {
            return MemberResponse.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .nickname(member.getNickname())
                    .phone(member.getFormattedPhone())
                    .email(member.getEmail())
                    .gender(member.getGender() != null ? member.getGender().getName() : "")
                    .orderNo(member.getOrder() != null ? member.getOrder().getOrderNo() : "")
                    .build();
        }
    }

    @Builder
    @Getter
    public static class MemberPageResponse {
        private Page<MemberResponse> page;

        public static MemberPageResponse from(Members members) {
            return MemberPageResponse.builder()
                    .page(members.getPage().map(MemberResponse::from))
                    .build();
        }
    }
}
