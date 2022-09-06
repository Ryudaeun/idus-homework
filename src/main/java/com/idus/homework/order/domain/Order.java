package com.idus.homework.order.domain;

import com.idus.homework.common.entity.BaseEntity;
import com.idus.homework.common.util.DateUtil;
import com.idus.homework.member.domain.Member;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

    @Column(nullable = false, length = 12, unique = true)
    private String orderNo;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(nullable = false, length = 100)
    private ZonedDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    public String makeOrderNo() {
        /*
         *  주문번호 발번 규칙
         *  "I" + 오늘날짜(yyMMdd) + 랜덤문자열(숫자포함) 5자리
         *  ex) I220905A1B1C
         */
        ZonedDateTime today = DateUtil.getNow();
        String orderNo = "I" + today.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String random = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        return orderNo + random;
    }
}
