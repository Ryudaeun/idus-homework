package com.idus.homework.member.presentation;

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

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberApplicationService memberApplicationService;
    private final JwtProvider jwtProvider;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody MemberDto.MemberRequest memberRequest) {
        memberService.signUp(memberRequest.toDomain());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request) {
        jwtProvider.logout(request);
    }

    @GetMapping("/member/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MemberDto.MemberResponse getMember(@Valid @PathVariable("id") Long id) {
        Member member = memberService.getMember(id);
        return MemberDto.MemberResponse.from(member);
    }

    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public MemberDto.MemberPageResponse getMembers(
            @Valid @RequestBody MemberSearchDto memberSearchDto,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Members members = memberApplicationService.getMembers(memberSearchDto, pageable);
        return MemberDto.MemberPageResponse.from(members);
    }
}
