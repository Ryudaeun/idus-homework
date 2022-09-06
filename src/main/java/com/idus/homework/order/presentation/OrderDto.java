package com.idus.homework.order.presentation;

import com.idus.homework.common.util.DateUtil;
import com.idus.homework.order.domain.Order;
import com.idus.homework.order.domain.Orders;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class OrderRequest {
        @NotBlank(message = "제품명을 입력해주세요.")
        @Size(min = 1, max = 100, message = "제품명은 1~100자 내외로 입력해주세요.")
        private String productName;

        @NotBlank(message = "결제일시를 입력해주세요.")
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
        private String paymentDate;

        @NotNull(message = "주문한 회원 정보가 없습니다.")
        private Long memberId;

        public Order toDomain() {
            return Order.builder()
                    .productName(productName)
                    .paymentDate(DateUtil.parseDateTime(paymentDate))
                    .build();
        }
    }

    @Builder
    @Getter
    public static class OrderResponse {
        private Long id;
        private String orderNo;
        private String productName;
        private String paymentDate;
        private String memberName;
        private String createdAt;
        private String updatedAt;

        public static OrderResponse from(Order order) {
            return OrderResponse.builder()
                    .id(order.getId())
                    .orderNo(order.getOrderNo())
                    .productName(order.getProductName())
                    .paymentDate(DateUtil.getStringWithZone(order.getPaymentDate()))
                    .memberName(order.getMember().getName())
                    .createdAt(DateUtil.getStringWithZone(order.getCreatedAt()))
                    .updatedAt(DateUtil.getStringWithZone(order.getUpdatedAt()))
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
