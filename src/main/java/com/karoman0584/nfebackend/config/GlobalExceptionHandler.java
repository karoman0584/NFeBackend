package com.karoman0584.nfebackend.config;

import com.karoman0584.nfebackend.exception.BusinessException;
import com.karoman0584.nfebackend.service.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDTO> handleBusinessException(BusinessException ex, final HttpServletRequest request) {
        ResponseDTO response = new ResponseDTO(ex.getMessage(), ex.getStatus().value());
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleSystemException(Exception ex, final HttpServletRequest request) {
        ResponseDTO response = new ResponseDTO(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
