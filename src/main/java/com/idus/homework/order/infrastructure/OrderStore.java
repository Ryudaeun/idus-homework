package com.idus.homework.order.infrastructure;

import com.idus.homework.order.domain.Order;

public interface OrderStore {
    Order save(Order order);
}
