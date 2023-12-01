package com.ll.netmong.base.exceptionhandler;

import com.ll.netmong.common.ProductException;
import com.ll.netmong.common.RsData;
import com.ll.netmong.domain.member.exception.NotMatchPasswordException;
import com.ll.netmong.domain.product.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String INVALID_PRODUCT_REQUEST = "유효하지 않은 요청 입니다.";

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RsData handleAccountNotFound(AccountNotFoundException e) {
        return RsData.failOf(e.getMessage());
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public RsData handleNotMatchPassword(NotMatchPasswordException e) {
        return RsData.failOf(e.getMessage());
    }

    // 그 외 예외
    @ExceptionHandler(ProductException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleProductNotExist(ProductException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getProductErrorCode());

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(exception.getProductErrorCode().getStatusCode()));
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleMultipart(ProductException exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getProductErrorCode());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidRequest(MethodArgumentNotValidException exception) {
        String message = Optional.ofNullable(exception.getBindingResult().getFieldError())
                .map(FieldError::getDefaultMessage)
                .orElseThrow(() -> new RuntimeException(INVALID_PRODUCT_REQUEST));

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}