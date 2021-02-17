package com.genkaipg.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SessionErrorDto {
    List<ErrorMessageDto> errors;
}
