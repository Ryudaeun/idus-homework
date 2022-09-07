package com.idus.homework.member.presentation;

import com.idus.homework.common.ResponseDto;
import com.idus.homework.member.application.MemberApplicationService;
import com.idus.homework.member.application.MemberService;
import com.idus.homework.member.domain.Member;
import com.idus.homework.member.domain.Members;
import com.idus.homework.member.dto.MemberSearchDto;
import com.idus.homework.security.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Tag(name = "Member", description = "회원 API")
    @Operation(summary = "회원가입", description = "신규 회원 정보를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/Response201"))),
            @ApiResponse(responseCode = "400", description = "잘못된 회원가입 정보 입력",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/Response400"))),
            @ApiResponse(responseCode = "400", description = "중복된 아이디 입력",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/Response400")))
    })
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto signUp(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "회원가입 정보",
                    content = @Content(schema = @Schema(implementation = MemberDto.MemberRequest.class)))
            @Valid @RequestBody MemberDto.MemberRequest memberRequest
    ) {
        Member member = memberService.signUp(memberRequest.toDomain());
        return ResponseDto.builder()
                .message("회원가입이 완료되었습니다.")
                .status(HttpStatus.CREATED.value())
                .data(MemberDto.MemberResponse.from(member))
                .build();
    }

    @Tag(name = "Member", description = "회원 API")
    @Operation(summary = "로그아웃", description = "로그아웃합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/Response200"))),
            @ApiResponse(responseCode = "401", description = "로그아웃 실패 - 만료된 토큰입니다.",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/Response401")))
    })
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto logout(HttpServletRequest request) {
        jwtProvider.logout(request);
        return ResponseDto.builder()
                .message("로그아웃 되었습니다.")
                .status(HttpStatus.OK.value())
                .build();
    }

    @Tag(name = "Member", description = "회원 API")
    @Operation(summary = "회원 상세 조회", description = "특정 회원의 정보를 상세 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberDto.MemberResponse.class))),
            @ApiResponse(responseCode = "400", description = "회원 id를 입력하지 않았거나 잘못된 형식으로 입력",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/Response400"))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원 조회",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/Response404")))
    })
    @GetMapping("/member/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MemberDto.MemberResponse getMember(
            @Parameter(name = "id", description = "회원 id", in = ParameterIn.PATH)
            @NotNull(message = "회원 id를 입력해주세요.") @Positive(message = "회원 id는 숫자만 입력 가능합니다.") @PathVariable("id") Long id
    ) {
        Member member = memberApplicationService.getMember(id);
        return MemberDto.MemberResponse.from(member);
    }

    @Tag(name = "Member", description = "회원 API")
    @Operation(summary = "회원 목록 조회", description = "가입된 회원들의 목록을 일정 페이지 단위로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberDto.MemberPageResponse.class)))
    })
    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public MemberDto.MemberPageResponse getMembers(
            @Parameter(name = "memberSearchDto", description = "검색조건", in = ParameterIn.QUERY)
            @Valid @RequestParam MemberSearchDto memberSearchDto,
            @Parameter(name = "pageable", description = "페이지네이션", in = ParameterIn.QUERY)
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Members members = memberApplicationService.getMembers(memberSearchDto, pageable);
        return MemberDto.MemberPageResponse.from(members);
    }
}
