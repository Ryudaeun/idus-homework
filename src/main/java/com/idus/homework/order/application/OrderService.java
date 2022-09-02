package com.idus.homework.order.application;

import com.idus.homework.order.domain.Order;
import com.idus.homework.order.domain.Orders;

public interface OrderService {
    Orders getOrders(Long memberId);
    Order getLastOrder(Long memberId);
    Order addOrder(Order order);
}
