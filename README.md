# 백패커/아이디어스 개발과제

## 개요

회원 주문 플랫폼의 Back-end 설계 및 API 개발

---

## ****Prerequisites****

- Java
- JDK 11
- Spring Boot 2.7.3
- Gradle
- JPA
- MySQL

### Run in development

```shell
# run project
./gradlew bootRun
```

### Page
- URL: http://localhost:8080
- OpenAPI: http://localhost:8080/swagger-ui.html

### Project Structure
```text
com.idus.homework
└───{도메인명}
│   └───application
│   │   │   {도메인명}ApplicationService  // Application Service
│   │   │   {도메인명}Service             // Domain Service Interface
│   │   │   {도메인명}ServiceImpl         // Domain Service Implement
│   └───domain
│   │   │   {도메인명}                    // Root Domain, Sub Domain, First Class Collection
│   └───infrastructure 
│   │   │   {도메인명}Repository          // Repository
│   │   │   {도메인명}Reader              // Reader Interface
│   │   │   {도메인명}ReaderImpl          // Reader Implement
│   │   │   {도메인명}Store               // Store Interface
│   │   │   {도메인명}StoreImpl           // Store Implement
│   └───presentation
│   │   │   {도메인명}Controller          // Controller
│   │   │   {도메인명}Dto                 // API Request / Response DTO
```
---

## Requirement

- 회원 가입
- 회원 로그인(인증)
- 회원 로그아웃
- 단일 회원 상세 정보 조회
- 단일 회원의 주문 목록 조회
- 여러 회원 목록 조회 :
    - 페이지네이션으로 일정 단위로 조회합니다.
    - 이름, 이메일을 이용하여 검색 기능이 필요합니다.
    - 각 회원의 마지막 주문 정보

## Entity

### Member

- 회원 정보를 저장하는 테이블

  | name       | type          | key | nullable | description        |
  | --- | --- | --- | --- | --- |
  | id         | bigint        | PK  | false    | 회원 id              |
  | username   | varchar(30)   |     | false    | 로그인 아이디            |
  | password   | text          |     | false    | 비밀번호               |
  | name       | varchar(20)   |     | false    | 이름                 |
  | nickname   | varchar(30)   |     | false    | 별명                 |
  | phone      | varchar(20)   |     | false    | 전화번호               |
  | email      | varchar(100)  |     | false    | 이메일                |
  | gender     | char(1)       |     | true     | 성별 (F: 여자 / M: 남자) |
  | role       | varchar(5)    |     | false    | 권한                 |
  | created_at | datetime      |     | false    | 생성일시                 |
  | updated_at | datetime      |     | true     | 수정일시                 |

### Order

- 주문 정보를 저장하는 테이블

  | name         | type         | key | nullable | description   |
  | --- | --- | --- | --- | --- |
  | id           | bigint       | PK  | false    | 주문 id         |
  | order_no     | varchar(12)  |     | false    | 주문번호          |
  | product_name | varchar(100) |     | false    | 제품명           |
  | payment_date | datetime     |     | false    | 결제일시          |
  | member_id    | bigint       |     | false    | 회원 id         |
  | created_at   | datetime     |     | false    | 생성일시          |
  | updated_at   | datetime     |     | true     | 수정일시          |

---

### Reference

- DB Replication: https://eddies.tistory.com/35