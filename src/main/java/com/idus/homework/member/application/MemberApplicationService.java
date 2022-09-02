package com.idus.homework.member.application;

import com.idus.homework.member.domain.Members;
import com.idus.homework.member.dto.MemberSearchDto;
import com.idus.homework.order.application.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberApplicationService {
    private final MemberService memberService;
    private final OrderService orderService;

    public Members getMembers(MemberSearchDto memberSearchDto, Pageable pageable) {
        Members members = memberService.getMembers(memberSearchDto, pageable);
        return new Members(members.getPage().map(member -> {
            member.setOrder(orderService.getLastOrder(member.getId()));
            return member;
        }));
    }
}
