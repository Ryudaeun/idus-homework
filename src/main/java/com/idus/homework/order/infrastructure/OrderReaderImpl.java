package com.idus.homework.order.infrastructure;

import com.idus.homework.order.domain.Order;
import com.idus.homework.order.domain.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
@RequiredArgsConstructor
public class OrderReaderImpl implements OrderReader {
    private final OrderRepository orderRepository;

    @Override
    public Orders findAllByMemberId(Long memberId) {
        return new Orders(orderRepository.findAllByMemberId(memberId));
    }

    @Override
    public Order findLastOrder(Long memberId) {
        return orderRepository.findTop1ByMemberIdOrderByCreatedAtDesc(memberId).orElse(null);
    }

    @Override
    public int countForOrderNo(Long memberId, ZonedDateTime start, ZonedDateTime end) {
        return orderRepository.countByMemberIdAndCreatedAtBetween(memberId, start, end);
    }
}
