package com.genkaipg.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordCryptResponseDto {
    private String password;
    private String crypt;
}
