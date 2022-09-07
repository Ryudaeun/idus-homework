package com.idus.homework.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    @Schema(description = "현재일시")
    @Builder.Default
    private String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    @Schema(description = "오류코드 - 오류 발생 시에만 표시")
    private String code;
    @Schema(description = "메시지 - 오류 발생 혹은 정상 처리 완료 시 메시지 표시")
    private String message;
    @Schema(description = "메시지 목록 - 2건 이상의 오류 발생 시 메시지 목록 표시")
    private List<String> messages;
    @Schema(description = "상태코드")
    private int status;
    @Schema(description = "반환 데이터 - 정상 처리 완료 후 반환되는 데이터 표시")
    private Object data;
}
