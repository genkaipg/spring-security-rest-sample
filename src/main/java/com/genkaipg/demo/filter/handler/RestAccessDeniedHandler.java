package com.genkaipg.demo.filter.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genkaipg.demo.dto.ErrorMessageDto;
import com.genkaipg.demo.dto.SessionErrorDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 権限によるアクセス拒否時
 */
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private String tokenPrefix;
    private String authenticateHeader;

    public RestAccessDeniedHandler(String tokenPrefix,String authenticateHeader){
        this.tokenPrefix=tokenPrefix;
        this.authenticateHeader=authenticateHeader;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        String value = tokenPrefix + " " + "error=\"insufficient_scope\"";
        response.setHeader(authenticateHeader, value);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(
                new SessionErrorDto(new ArrayList<>(){{add(new ErrorMessageDto(value));}})));
    }
}
