CREATE DATABASE `idus_homework` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- idus_homework.`member` definition
CREATE TABLE `member` (
          `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '회원 ID',
          `username` varchar(30) NOT NULL COMMENT '로그인 아이디',
          `password` text NOT NULL COMMENT '로그인 비밀번호',
          `name` varchar(20) NOT NULL COMMENT '회원 이름',
          `nickname` varchar(30) NOT NULL COMMENT '회원 별명',
          `phone` varchar(20) NOT NULL COMMENT '회원 전화번호',
          `email` varchar(100) NOT NULL COMMENT '회원 이메일',
          `gender` varchar(1) DEFAULT NULL COMMENT '회원 성별(F: 여자, M: 남자)',
          `role` varchar(5) NOT NULL COMMENT '권한',
          `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
          `updated_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '수정일시',
          UNIQUE KEY `member_username_uk` (`username`)
) COMMENT '회원 테이블' CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- idus_homework.orders definition
CREATE TABLE `orders` (
          `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '주문 ID',
          `order_no` varchar(12)  NOT NULL COMMENT '주문번호',
          `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '제품명',
          `payment_date` datetime NOT NULL COMMENT '결제일시',
          `member_id` bigint DEFAULT NULL COMMENT '회원 ID',
          `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
          `updated_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '수정일시',
          UNIQUE KEY `order_order_no_uk` (`order_no`),
          CONSTRAINT `order_member_id_fk` FOREIGN KEY (`member_id`) REFERENCES `idus_homework`.`member` (`id`)
) COMMENT '주문 테이블' CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;