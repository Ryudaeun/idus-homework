package com.idus.homework.member.infrastructure;

import com.idus.homework.member.domain.Member;

public interface MemberStore {
    Member save(Member member);
}
