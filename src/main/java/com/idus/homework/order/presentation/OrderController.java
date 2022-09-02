package com.idus.homework.order.presentation;

import com.idus.homework.order.application.OrderService;
import com.idus.homework.order.domain.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/orders/{member-id}")
    public OrderDto.OrderListResponse getOrders(@Valid @PathVariable("member-id") Long memberId) {
        Orders orders = orderService.getOrders(memberId);
        return OrderDto.OrderListResponse.from(orders);
    }
}
