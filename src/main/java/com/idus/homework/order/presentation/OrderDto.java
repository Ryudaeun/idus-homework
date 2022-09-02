package com.idus.homework.order.presentation;

import com.idus.homework.order.domain.Order;
import com.idus.homework.order.domain.Orders;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    @Builder
    @Getter
    public static class OrderResponse {
        private Long id;
        private String orderNo;
        private String productName;
        private String paymentDate;
        private String memberName;
        private String createdAt;

        public static OrderResponse from(Order order) {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            return OrderResponse.builder()
                    .id(order.getId())
                    .orderNo(order.getOrderNo())
                    .productName(order.getProductName())
                    .paymentDate(order.getPaymentDate().format(formatter))
                    .memberName(order.getMember().getName())
                    .createdAt(order.getCreatedAt().format(formatter))
                    .build();
        }
    }

    @Builder
    @Getter
    public static class OrderListResponse {
        private List<OrderResponse> list;

        public static OrderListResponse from(Orders orders) {
            return OrderListResponse.builder()
                    .list(orders.getList().stream().map(OrderResponse::from).collect(Collectors.toList()))
                    .build();
        }
    }
}
