package com.idus.homework.order.infrastructure;

import com.idus.homework.order.domain.Order;
import com.idus.homework.order.domain.Orders;

import java.time.ZonedDateTime;

public interface OrderReader {
    Orders findAllByMemberId(Long memberId);
    Order findLastOrder(Long memberId);
    int countForOrderNo(Long memberId, ZonedDateTime start, ZonedDateTime end);
}
