package com.idus.homework.member.infrastructure;

import com.idus.homework.member.domain.Member;
import com.idus.homework.member.domain.Members;
import com.idus.homework.member.dto.MemberSearchDto;
import org.springframework.data.domain.Pageable;

public interface MemberReader {
    Member findByUsername(String username);
    Member findById(Long id);
    Members findAll(MemberSearchDto memberSearchDto, Pageable pageable);
}
