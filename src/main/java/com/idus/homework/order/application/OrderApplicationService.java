package com.idus.homework.order.application;

import com.idus.homework.member.application.MemberService;
import com.idus.homework.member.domain.Member;
import com.idus.homework.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderApplicationService {
    private final OrderService orderService;
    private final MemberService memberService;

    public Order addOrder(Order order, Long memberId) {
        Member member = memberService.getMember(memberId);

        Order newOrder = Order.builder()
                .orderNo(order.makeOrderNo())
                .paymentDate(order.getPaymentDate())
                .productName(order.getProductName())
                .member(member)
                .build();

        return orderService.addOrder(newOrder);
    }
}
