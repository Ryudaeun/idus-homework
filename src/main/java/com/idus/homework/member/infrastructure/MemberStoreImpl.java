package com.idus.homework.member.infrastructure;

import com.idus.homework.member.domain.Member;
import com.idus.homework.member.domain.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberReaderImpl implements MemberReader {
    private final MemberRepository memberRepository;

    @Override
    public Member findByLoginId(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Override
    public Members findAllByName(String name) {
        return new Members(memberRepository.findAllByName(name));
    }

    @Override
    public Members findAllByEmail(String email) {
        return new Members(memberRepository.findAllByEmail(email));
    }
}
