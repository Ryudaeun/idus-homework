package com.idus.homework.order.infrastructure;

import com.idus.homework.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByMemberId(Long memberId);
    Optional<Order> findTop1ByMemberIdOrderByCreatedAtDesc(Long memberId);
}
