package com.genkaipg.demo.controller;

import com.genkaipg.demo.dto.HelloResponseDto;
import com.genkaipg.demo.dto.PasswordCryptResponseDto;
import com.genkaipg.demo.dto.SampleAdminRegistRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SampleController {

    private final PasswordEncoder passwordEncoder;

    public SampleController(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/admin/test")
    public HelloResponseDto adminTest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new HelloResponseDto("Hello "+authentication.getName()+". You are admin!");
    }

    @GetMapping(value = "/user/test")
    public HelloResponseDto userTest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new HelloResponseDto("Hello "+authentication.getName()+". You are user!");
    }

    @PostMapping(value = "/all/crypt",produces= MediaType.APPLICATION_JSON_VALUE)
    public PasswordCryptResponseDto adminRegist(@RequestBody SampleAdminRegistRequestDto sampleAdminRegistRequestDto) {
        String digest = passwordEncoder.encode(sampleAdminRegistRequestDto.getPassword());
        log.debug("password = "+ sampleAdminRegistRequestDto.getPassword());
        log.debug("digest = "+ digest);

        return new PasswordCryptResponseDto(sampleAdminRegistRequestDto.getPassword(),digest);
    }
}
