package com.idus.homework.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    @Builder.Default
    private String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    private String code;
    private String message;
    private List<String> messages;
    private int status;
    private Object data;
}
