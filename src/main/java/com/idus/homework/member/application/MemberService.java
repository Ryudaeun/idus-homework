package com.idus.homework.member.application;

import com.idus.homework.member.domain.Member;
import com.idus.homework.member.domain.Members;
import com.idus.homework.member.dto.MemberSearchDto;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    Member getMemberByUsername(String username);
    Member getMember(Long id);
    Members getMembers(MemberSearchDto memberSearchDto, Pageable pageable);
    Member signUp(Member member);
}
