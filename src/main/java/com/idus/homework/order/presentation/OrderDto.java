package com.idus.homework.order.presentation;

import com.idus.homework.common.util.DateUtil;
import com.idus.homework.order.domain.Order;
import com.idus.homework.order.domain.Orders;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "제품명", example = "추석 선물세트🌝")
        @NotBlank(message = "제품명을 입력해주세요.")
        @Size(min = 1, max = 100, message = "제품명은 1~100자 내외로 입력해주세요.")
        private String productName;

        @Schema(description = "결제일시", example = "yyyy-MM-dd HH:mm:ss")
        @NotBlank(message = "결제일시를 입력해주세요.")
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
        private String paymentDate;

        @Schema(description = "주문한 회원 id", example = "1")
        @NotNull(message = "회원 id를 입력해주세요.")
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
        @Schema(description = "주문 id", example = "1")
        private Long id;
        @Schema(description = "주문번호", example = "I220907ABCDE")
        private String orderNo;
        @Schema(description = "제품명", example = "추석 선물세트🌝")
        private String productName;
        @Schema(description = "결제일시", example = "yyyy-MM-dd HH:mm:ss")
        private String paymentDate;
        @Schema(description = "주문한 회원 이름", example = "류다은")
        private String memberName;
        @Schema(description = "등록일시", example = "yyyy-MM-dd HH:mm:ss")
        private String createdAt;
        @Schema(description = "수정일시", example = "yyyy-MM-dd HH:mm:ss")
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
