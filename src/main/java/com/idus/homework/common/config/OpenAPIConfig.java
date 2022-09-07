package com.idus.homework.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

@Component
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Contact contact = new Contact().name("daeun-ryu")
                .url("https://github.com/Ryudaeun/idus-homework")
                .email("so.difficult0121@gmail.com");

        Info info = new Info()
                .version(appVersion)
                .title("Idus Homework API")
                .description("백패커/아이디어스 개발과제 - 회원/주문 관리 API")
                .termsOfService("https://swagger.io/terms/")
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .components(new Components()
                        .addSchemas("Response200", getSchema("처리 성공 관련 메시지", 200))
                        .addSchemas("Response201", getSchema("등록 완료 관련 메시지", 201, "반환 데이터"))
                        .addSchemas("Response204", getSchema("삭제 완료 관련 메시지", 204))
                        .addSchemas("Response400",
                                getSchema("BAD_REQUEST(잘못된 요청) 관련 오류 코드", "BAD_REQUEST(잘못된 요청) 관련 오류 메시지", 400))
                        .addSchemas("Response401",
                                getSchema("UNAUTHORIZED(유효한 인증 자격 부족) 관련 오류 코드", "UNAUTHORIZED(유효한 인증 자격 부족) 관련 오류 메시지", 401))
                        .addSchemas("Response404",
                                getSchema("NOT_FOUND(존재하지 않는 리소스) 관련 오류 코드", "NOT_FOUND(존재하지 않는 리소스) 관련 오류 메시지", 404))
                );
    }

    @Bean
    public OpenApiCustomiser sortSchemasAlphabetically() {
        return openApi -> {
            Map<String, Schema> schemas = openApi.getComponents().getSchemas();
            openApi.getComponents().setSchemas(new TreeMap<>(schemas));
        };
    }

    private Schema getSchema(String message, int status) {
        return new Schema<Map<String, Object>>()
                .addProperty("message", new StringSchema().example(message))
                .addProperty("status", new IntegerSchema().example(status));
    }

    private Schema getSchema(String message, int status, Object data) {
        return new Schema<Map<String, Object>>()
                .addProperty("message", new StringSchema().example(message))
                .addProperty("status", new IntegerSchema().example(status))
                .addProperty("data", new ObjectSchema().example(data));
    }

    private Schema getSchema(String code, String message, int status) {
        return new Schema<Map<String, Object>>()
                .addProperty("code", new StringSchema().example(code))
                .addProperty("message", new StringSchema().example(message))
                .addProperty("status", new IntegerSchema().example(status));
    }
}
