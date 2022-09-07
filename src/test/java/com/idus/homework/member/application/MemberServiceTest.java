package com.idus.homework.member.application;

import com.idus.homework.common.ErrorCode;
import com.idus.homework.member.domain.Gender;
import com.idus.homework.member.domain.Member;
import com.idus.homework.member.domain.Role;
import com.idus.homework.member.infrastructure.*;
import com.idus.homework.member.presentation.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    MemberService memberService;
    MemberReader memberReader;
    MemberStore memberStore;
    PasswordEncoder passwordEncoder;
    @Mock
    MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        memberReader = new MemberReaderImpl(memberRepository);
        memberStore = new MemberStoreImpl(memberRepository);
        passwordEncoder = new BCryptPasswordEncoder();
        memberService = new MemberServiceImpl(memberReader, memberStore, passwordEncoder);
    }

    @Test
    void 단일_회원_조회_username() {
        // given
        Member member = makeMember();
        String username = member.getUsername();

        // mock
        given(memberRepository.findByUsername(username)).willReturn(Optional.of(member));

        // when
        Member findMember = memberService.getMemberByUsername(username);

        // then
        assertEquals(member.getId(), findMember.getId());
        assertEquals(member.getUsername(), findMember.getUsername());
    }

    @Test
    void 단일_회원_조회_id() {
        // given
        Member member = makeMember();
        Long memberId = member.getId();

        // mock
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        Member findMember = memberService.getMember(1L);

        // then
        assertEquals(member.getId(), findMember.getId());
        assertEquals(member.getUsername(), findMember.getUsername());
    }

    @Test
    void 존재하지_않는_회원_조회() {
        // given

        // mock
        given(memberRepository.findById(2L)).willReturn(Optional.empty());

        // then
        String expectMessage = ErrorCode.MEMBER_NOT_EXISTS.getMessage();
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            // when
            memberService.getMember(2L);
        });

        assertEquals(expectMessage, exception.getMessage());
    }

    @Test
    void 회원가입() {
        // given
        MemberDto.MemberRequest memberRequest = makeMemberRequest();
        Member member = memberRequest.toDomain();
        String password = member.getPassword();

        // mock
        given(memberRepository.save(any())).willReturn(makeMember());

        // when
        Member newMember = memberService.signUp(member);

        // then
        assertEquals(member.getUsername(), newMember.getUsername());
        assertTrue(passwordEncoder.matches(password, newMember.getPassword()));
        assertEquals(member.getName(), newMember.getName());
        assertEquals(member.getPhone(), newMember.getPhone());
        assertEquals(member.getEmail(), newMember.getEmail());
    }

    @Test
    void 중복된_로그인_아이디_입력() {
        // given
        MemberDto.MemberRequest memberRequest = makeMemberRequest();
        Member member = memberRequest.toDomain();

        // mock
        given(memberRepository.save(any())).willThrow(DataIntegrityViolationException.class);

        // then
        String expectMessage = ErrorCode.ALREADY_EXISTS_USERNAME.getMessage();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // when
            memberService.signUp(member);
        });

        assertEquals(expectMessage, exception.getMessage());
    }

    private MemberDto.MemberRequest makeMemberRequest() {
        MemberDto.MemberRequest memberRequest = new MemberDto.MemberRequest();

        memberRequest.setUsername("tester");
        memberRequest.setPassword("Password1@");
        memberRequest.setName("테스터");
        memberRequest.setNickname("테스터 별명");
        memberRequest.setPhone("01012345678");
        memberRequest.setEmail("example@email.com");
        memberRequest.setGender(Gender.M);

        return memberRequest;
    }

    private Member makeMember() {
        return Member.builder()
                .id(1L)
                .username("tester")
                .password(passwordEncoder.encode("Password1@"))
                .name("테스터")
                .nickname("테스터 별명")
                .phone("01012345678")
                .email("example@email.com")
                .gender(Gender.M)
                .role(Role.USER)
                .build();
    }
}