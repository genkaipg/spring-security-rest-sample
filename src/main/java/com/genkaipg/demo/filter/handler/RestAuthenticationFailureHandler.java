package com.genkaipg.demo.filter.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genkaipg.demo.dto.ErrorMessageDto;
import com.genkaipg.demo.dto.SessionErrorDto;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 認証失敗時
 */
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String tokenPrefix;
    private final String authenticateHeader;

    public RestAuthenticationFailureHandler(String tokenPrefix,String authenticateHeader){
        this.tokenPrefix=tokenPrefix;
        this.authenticateHeader=authenticateHeader;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException{
        String value = tokenPrefix + " " + "error=\"login_incorrect\"";
        response.setHeader(authenticateHeader, value);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(
                new SessionErrorDto(new ArrayList<>(){{add(new ErrorMessageDto(value));}})));
    }
}
