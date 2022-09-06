package com.idus.homework.member.presentation;

import com.idus.homework.common.ResponseDto;
import com.idus.homework.member.application.MemberApplicationService;
import com.idus.homework.member.application.MemberService;
import com.idus.homework.member.domain.Member;
import com.idus.homework.member.domain.Members;
import com.idus.homework.member.dto.MemberSearchDto;
import com.idus.homework.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberApplicationService memberApplicationService;
    private final JwtProvider jwtProvider;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto signUp(@Valid @RequestBody MemberDto.MemberRequest memberRequest) {
        Member member = memberService.signUp(memberRequest.toDomain());
        return ResponseDto.builder()
                .message("회원가입이 완료되었습니다.")
                .status(HttpStatus.CREATED.value())
                .data(MemberDto.MemberResponse.from(member))
                .build();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseDto logout(HttpServletRequest request) {
        jwtProvider.logout(request);
        return ResponseDto.builder()
                .message("로그아웃 되었습니다.")
                .status(HttpStatus.NO_CONTENT.value())
                .build();
    }

    @GetMapping("/member/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MemberDto.MemberResponse getMember(
            @NotNull(message = "회원 id를 입력해주세요.") @Positive @PathVariable("id") Long id
    ) {
        Member member = memberApplicationService.getMember(id);
        return MemberDto.MemberResponse.from(member);
    }

    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public MemberDto.MemberPageResponse getMembers(
            @Valid @RequestBody MemberSearchDto memberSearchDto,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Members members = memberApplicationService.getMembers(memberSearchDto, pageable);
        return MemberDto.MemberPageResponse.from(members);
    }
}
