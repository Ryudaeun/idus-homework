package com.idus.homework.order.presentation;

import com.idus.homework.common.ResponseDto;
import com.idus.homework.order.application.OrderApplicationService;
import com.idus.homework.order.application.OrderService;
import com.idus.homework.order.domain.Order;
import com.idus.homework.order.domain.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderApplicationService orderApplicationService;

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto addOrder(@Valid @RequestBody OrderDto.OrderRequest orderRequest) {
        Order order = orderApplicationService.addOrder(orderRequest.toDomain(), orderRequest.getMemberId());
        return ResponseDto.builder()
                .message("주문 등록이 완료되었습니다.")
                .status(HttpStatus.CREATED.value())
                .data(OrderDto.OrderResponse.from(order))
                .build();
    }

    @GetMapping("/orders/{member-id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto.OrderListResponse getOrders(
            @NotNull(message = "회원 id를 입력해주세요.") @Positive @PathVariable("member-id") Long memberId
    ) {
        Orders orders = orderService.getOrders(memberId);
        return OrderDto.OrderListResponse.from(orders);
    }
}
