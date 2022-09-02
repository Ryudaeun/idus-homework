package com.idus.homework.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Orders {
    private final List<Order> list;
}
