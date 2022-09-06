package com.idus.homework.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idus.homework.common.ErrorCode;
import com.idus.homework.common.ResponseDto;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorCode errorCode = ErrorCode.valueOf(Objects.requireNonNullElseGet(exception, ErrorCode.UNKNOWN_ERROR::name));

        ResponseDto errorPayload = ResponseDto.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .status(response.getStatus())
                .build();

        response.getWriter().print(new ObjectMapper().writeValueAsString(errorPayload));
    }
}
