package com.idus.homework.common.handler;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class InterceptorExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> exceptionHandler(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> exceptionHandler(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({BindException.class, IllegalArgumentException.class, ConstraintViolationException.class})
    public ResponseEntity<String> exceptionHandler(Exception e) {
        String message = e.getMessage();
        if (e instanceof BindException) {
            message = getDefaultMessage((BindException) e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> exceptionHandler(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("요청 객체(JSON type)를 확인해주세요.");
    }

    private String getDefaultMessage(BindException e) {
        List<String> messageList = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        if (messageList.size() == 1) {
            return messageList.get(0);
        } else {
            return messageList.toString();
        }
    }
}
