package com.idus.homework.member.application;

import com.idus.homework.common.ErrorCode;
import com.idus.homework.member.domain.Member;
import com.idus.homework.member.domain.Members;
import com.idus.homework.member.dto.MemberSearchDto;
import com.idus.homework.member.infrastructure.MemberReader;
import com.idus.homework.member.infrastructure.MemberStore;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberReader memberReader;
    private final MemberStore memberStore;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Member getMemberByUsername(String username) {
        Member member = memberReader.findByUsername(username);
        if (member == null) {
            throw new NoSuchElementException(ErrorCode.MEMBER_NOT_EXISTS.getMessage());
        }
        return member;
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMember(Long id) {
        Member member = memberReader.findById(id);
        if (member == null) {
            throw new NoSuchElementException(ErrorCode.MEMBER_NOT_EXISTS.getMessage());
        }
        return member;
    }

    @Override
    @Transactional(readOnly = true)
    public Members getMembers(MemberSearchDto memberSearchDto, Pageable pageable) {
        return memberReader.findAll(memberSearchDto, pageable);
    }

    @Override
    @Transactional
    public Member signUp(Member member) {
        member.setUserRole();
        member.encodePassword(passwordEncoder);

        try {
            return memberStore.save(member);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new IllegalArgumentException(ErrorCode.ALREADY_EXISTS_USERNAME.getMessage());
        }
    }
}
