package com.idus.homework.order.application;

import com.idus.homework.order.domain.Order;
import com.idus.homework.order.domain.Orders;
import com.idus.homework.order.infrastructure.OrderReader;
import com.idus.homework.order.infrastructure.OrderStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderReader orderReader;
    private final OrderStore orderStore;

    @Override
    public Orders getOrders(Long memberId) {
        return orderReader.findAllByMemberId(memberId);
    }

    @Override
    public Order getLastOrder(Long memberId) {
        return orderReader.findLastOrder(memberId);
    }

    @Override
    public Order addOrder(Order order) {
        return orderStore.save(order);
    }
}
