package com.idus.homework.member.presentation;

import com.idus.homework.common.util.DateUtil;
import com.idus.homework.member.domain.Gender;
import com.idus.homework.member.domain.Member;
import com.idus.homework.member.domain.Members;
import com.idus.homework.member.domain.Role;
import com.idus.homework.order.domain.Order;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "로그인 아이디", minLength = 5, maxLength = 30, example = "daeun-ryu")
        @Size(min = 5, max = 30, message = "아이디는 5~30자 내외로 입력해주세요.")
        private String username;

        @Schema(description = "로그인 비밀번호", minLength = 10, example = "Password1@")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[~!@#$%^&*()-_=+|]?)[A-Za-z[0-9]~!@#$%^&*()-_=+|]{10,}$",
                message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함하여 최소 10자 이상 작성해주세요."
        )
        private String password;

        @Schema(description = "이름", maxLength = 20, example = "류다은")
        @NotBlank(message = "이름을 입력해주세요.")
        @Size(max = 20, message = "이름은 최대 20자까지 입력 가능합니다.")
        private String name;

        @Schema(description = "별명", maxLength = 30, example = "류다은 별명")
        @NotBlank(message = "별명을 입력해주세요.")
        @Size(max = 30, message = "별명은 최대 30자까지 입력 가능합니다.")
        private String nickname;

        @Schema(description = "전화번호", minLength = 9, maxLength = 11, example = "01012345678")
        @Pattern(regexp = "^[0-9]{9,11}$", message = "전화번호는 9~11자 내외로 숫자만 입력해주세요.")
        private String phone;

        @Schema(description = "이메일", example = "example@email.com")
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "잘못된 이메일 형식입니다.")
        private String email;

        @Schema(description = "성별(F: 여성, M: 남성)", allowableValues = {"F", "M"}, example = "F")
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
                    .role(Role.USER)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class MemberOrderResponse {
        @Schema(description = "주문 id", example = "1")
        private Long id;
        @Schema(description = "주문번호", example = "I220907ABCDE")
        private String orderNo;
        @Schema(description = "제품명", example = "추석 선물세트🌝")
        private String productName;
        @Schema(description = "결제일시", example = "yyyy-MM-dd HH:mm:ss")
        private String paymentDate;

        public static MemberOrderResponse from(Order order) {
            return MemberOrderResponse.builder()
                    .id(order.getId())
                    .orderNo(order.getOrderNo())
                    .productName(order.getProductName())
                    .paymentDate(DateUtil.getStringWithZone(order.getPaymentDate()))
                    .build();
        }
    }

    @Builder
    @Getter
    public static class MemberResponse {
        @Schema(description = "회원 id", example = "1")
        private Long id;
        @Schema(description = "로그인 아이디", example = "daeun-ryu")
        private String username;
        @Schema(description = "이름", example = "류다은")
        private String name;
        @Schema(description = "별명", example = "류다은 별명")
        private String nickname;
        @Schema(description = "전화번호", example = "010-1234-5678")
        private String phone;
        @Schema(description = "이메일", example = "example@email.com")
        private String email;
        @Schema(description = "성별", allowableValues = {"여성", "남성"}, example = "여성")
        private String gender;
        @Schema(description = "등록일시", example = "yyyy-MM-dd HH:mm:ss")
        private String createdAt;
        @Schema(description = "수정일시", example = "yyyy-MM-dd HH:mm:ss")
        private String updatedAt;
        private MemberOrderResponse order;

        public static MemberResponse from(Member member) {
            return MemberResponse.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .name(member.getName())
                    .nickname(member.getNickname())
                    .phone(member.getFormattedPhone())
                    .email(member.getEmail())
                    .createdAt(DateUtil.getStringWithZone(member.getCreatedAt()))
                    .createdAt(DateUtil.getStringWithZone(member.getUpdatedAt()))
                    .gender(member.getGender() != null ? member.getGender().getName() : "")
                    .order(member.getOrder() != null ? MemberOrderResponse.from(member.getOrder()) : null)
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
