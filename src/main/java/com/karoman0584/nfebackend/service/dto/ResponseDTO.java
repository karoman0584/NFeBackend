package com.karoman0584.nfebackend.service.dto;

import lombok.Data;

@Data
public class ResponseDTO {
    private final String message;
    private final Integer status;
}
