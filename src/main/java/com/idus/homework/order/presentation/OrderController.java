package com.idus.homework.order.presentation;

import com.idus.homework.common.ResponseDto;
import com.idus.homework.order.application.OrderApplicationService;
import com.idus.homework.order.application.OrderService;
import com.idus.homework.order.domain.Order;
import com.idus.homework.order.domain.Orders;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Tag(name = "Order", description = "주문 API")
    @Operation(summary = "주문 등록", description = "신규 주문 정보를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주문 등록 성공",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/Response201"))),
            @ApiResponse(responseCode = "400", description = "잘못된 주문 정보 입력",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/Response400"))),
    })
    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto addOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "주문등록 정보",
                    content = @Content(schema = @Schema(implementation = OrderDto.OrderRequest.class)))
            @Valid @RequestBody OrderDto.OrderRequest orderRequest
    ) {
        Order order = orderApplicationService.addOrder(orderRequest.toDomain(), orderRequest.getMemberId());
        return ResponseDto.builder()
                .message("주문 등록이 완료되었습니다.")
                .status(HttpStatus.CREATED.value())
                .data(OrderDto.OrderResponse.from(order))
                .build();
    }

    @Tag(name = "Order", description = "주문 API")
    @Operation(summary = "단일 회원 주문 목록 조회", description = "특정 회원의 주문 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = OrderDto.OrderListResponse.class)))
    })
    @GetMapping("/orders/{member-id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto.OrderListResponse getOrders(
            @Parameter(name = "memberId", description = "회원 id", in = ParameterIn.PATH)
            @NotNull(message = "회원 id를 입력해주세요.") @Positive @PathVariable("member-id") Long memberId
    ) {
        Orders orders = orderService.getOrders(memberId);
        return OrderDto.OrderListResponse.from(orders);
    }
}
