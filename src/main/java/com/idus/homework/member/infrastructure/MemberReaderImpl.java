package com.idus.homework.member.infrastructure;

import com.idus.homework.member.domain.Member;
import com.idus.homework.member.domain.Members;
import com.idus.homework.member.dto.MemberSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberReaderImpl implements MemberReader {
    private final MemberRepository memberRepository;

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Override
    public Members findAll(MemberSearchDto memberSearchDto, Pageable pageable) {
        return new Members(memberRepository.findAll(searchMember(memberSearchDto), pageable));
    }

    public Specification<Member> searchMember(MemberSearchDto memberSearchDto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    builder.like(root.get("name").as(String.class), "%"+memberSearchDto.getName()+"%")
            );
            predicates.add(
                    builder.like(root.get("email").as(String.class), "%"+memberSearchDto.getEmail()+"%")
            );

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
