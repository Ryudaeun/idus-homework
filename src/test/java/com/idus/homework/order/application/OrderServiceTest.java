package com.idus.homework.order.application;

import com.idus.homework.common.util.DateUtil;
import com.idus.homework.member.domain.Member;
import com.idus.homework.order.domain.Order;
import com.idus.homework.order.domain.Orders;
import com.idus.homework.order.infrastructure.*;
import com.idus.homework.order.presentation.OrderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class OrderServiceTest {
    OrderService orderService;
    OrderReader orderReader;
    OrderStore orderStore;
    @Mock
    OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        orderReader = new OrderReaderImpl(orderRepository);
        orderStore = new OrderStoreImpl(orderRepository);
        orderService = new OrderServiceImpl(orderReader, orderStore);
    }

    @Test
    void 주문_목록_조회() {
        // given
        List<Order> orderList = List.of(makeOrder("I220907ABCDE"));
        Long memberId = 1L;

        // mock
        given(orderRepository.findAllByMemberId(memberId)).willReturn(orderList);

        // when
        Orders findAllOrders = orderService.getOrders(memberId);

        // then
        assertEquals(1, findAllOrders.getList().size());
    }

    @Test
    void 특정_회원의_마지막_주문_정보() {
        // given
        Order order = makeOrder("I220907ABCDE");
        Long memberId = order.getMember().getId();

        // mock
        given(orderRepository.findTop1ByMemberIdOrderByCreatedAtDesc(memberId)).willReturn(Optional.of(order));

        // when
        Order findLastOrder = orderService.getLastOrder(memberId);

        // then
        assertEquals(order.getOrderNo(), findLastOrder.getOrderNo());
        assertEquals(order.getProductName(), findLastOrder.getProductName());
        assertEquals(order.getPaymentDate(), findLastOrder.getPaymentDate());
        assertEquals(order.getMember().getId(), findLastOrder.getMember().getId());
    }

    @Test
    void 주문_등록() {
        // given
        OrderDto.OrderRequest orderRequest = makeOrderRequest();
        Order order = orderRequest.toDomain();

        String orderNo = order.makeOrderNo();

        order.setMember(makeMember());
        order.setOrderNo(orderNo);

        // mock
        given(orderRepository.save(any())).willReturn(makeOrder(orderNo));

        // when
        Order newOrder = orderService.addOrder(order);

        // then
        assertEquals(order.getOrderNo(), newOrder.getOrderNo());
        assertEquals(order.getProductName(), newOrder.getProductName());
        assertEquals(order.getPaymentDate(), newOrder.getPaymentDate());
        assertEquals(order.getMember().getId(), newOrder.getMember().getId());
    }

    private OrderDto.OrderRequest makeOrderRequest() {
        OrderDto.OrderRequest orderRequest = new OrderDto.OrderRequest();

        orderRequest.setMemberId(1L);
        orderRequest.setProductName("추석 선물세트🌝");
        orderRequest.setPaymentDate("2022-09-07 15:23:33");

        return orderRequest;
    }

    private Order makeOrder(String orderNo) {
        return Order.builder()
                .id(1L)
                .member(makeMember())
                .productName("추석 선물세트🌝")
                .paymentDate(DateUtil.parseDateTime("2022-09-07 15:23:33"))
                .orderNo(orderNo)
                .build();
    }

    private Member makeMember() {
        return Member.builder()
                .id(1L)
                .build();
    }
}