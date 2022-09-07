package com.idus.homework.common.handler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.idus.homework.common.ResponseDto;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class InterceptorExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto> conflictExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        ResponseDto errorPayload = ResponseDto.builder()
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .build();
        return new ResponseEntity<>(errorPayload, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ResponseDto> conflictExceptionHandler(TokenExpiredException e) {
        ResponseDto errorPayload = ResponseDto.builder()
                .message(e.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();
        return new ResponseEntity<>(errorPayload, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<ResponseDto> notFoundExceptionHandler(Exception e) {
        ResponseDto errorPayload = ResponseDto.builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(errorPayload, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BindException.class, IllegalArgumentException.class,
            ConstraintViolationException.class, TypeMismatchException.class})
    public ResponseEntity<ResponseDto> badRequestExceptionHandler(Exception e) {
        ResponseDto errorPayload;
        if (e instanceof BindException) {
            errorPayload = ResponseDto.builder()
                    .messages(getDefaultMessage((BindException) e))
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        } else {
            errorPayload = ResponseDto.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        return new ResponseEntity<>(errorPayload, HttpStatus.BAD_REQUEST);
    }

    private List<String> getDefaultMessage(BindException e) {
        return e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }
}
