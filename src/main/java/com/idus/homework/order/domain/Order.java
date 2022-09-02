package com.idus.homework.order.domain;

import com.idus.homework.common.entity.BaseEntity;
import com.idus.homework.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Table(name = "orders")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 12)
    private String orderNo;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(nullable = false, length = 100)
    private ZonedDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
}
