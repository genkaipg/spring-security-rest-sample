package com.genkaipg.demo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genkaipg.demo.dto.ErrorMessageDto;
import com.genkaipg.demo.dto.SessionErrorDto;
import com.genkaipg.demo.constants.SecurityConfigConstans;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * 認証失敗時の処理
 */
@Slf4j
public class RestAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint
{

    private final String authenticateHeaderName;
    private final String tokenPrefix;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RestAuthenticationEntryPoint(String tokenPrefix, String authenticateHeaderName, String loginUrl) {
        super(loginUrl);
        this.authenticateHeaderName=authenticateHeaderName;
        this.tokenPrefix=tokenPrefix;
    }

    /**
     * Ajaxで分岐
     * 違うならコンストラクタで設定したURLへforward
     */
    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        if (SecurityConfigConstans.XHR_FIELDS_IS_AJAX_VALUE.equals(request.getHeader(SecurityConfigConstans.XHR_FIELDS_IS_AJAX_KEY))) {
            String value;
            if (request.getRequestedSessionId() != null) {
                //期限切れ
                log.debug("invalid token: "+request.getRequestedSessionId());
                value = tokenPrefix + " " + "error=\"invalid_token\"";
            }else {
                //セッションなし
                log.debug("session none.");
                value = tokenPrefix + " " + "realm=\"Realm\"";
            }
            response.setHeader(this.authenticateHeaderName, value);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(
                    new SessionErrorDto(new ArrayList<>(){{add(new ErrorMessageDto(value));}})));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            super.commence(request, response, authException);
        }

    }
}