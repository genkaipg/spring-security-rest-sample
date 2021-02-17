package com.genkaipg.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {

    private String access_token;
    private String token_type;
    private Integer expires_in;

}
