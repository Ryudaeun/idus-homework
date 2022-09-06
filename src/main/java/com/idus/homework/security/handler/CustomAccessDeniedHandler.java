package com.idus.homework.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idus.homework.common.ErrorCode;
import com.idus.homework.common.ResponseDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ResponseDto errorPayload = ResponseDto.builder()
                .code(ErrorCode.ACCESS_DENIED.name())
                .message(ErrorCode.ACCESS_DENIED.getMessage())
                .status(response.getStatus())
                .build();

        response.getWriter().print(new ObjectMapper().writeValueAsString(errorPayload));
    }
}
