package com.genkaipg.demo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genkaipg.demo.dto.LoginResponseDto;
import com.genkaipg.demo.constants.SecurityConfigConstans;
import lombok.extern.slf4j.Slf4j;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * ログイン成功時にJsonで出力
 * Bearer等の Prefixも対応
 */
@Slf4j
public class PrefixHeaderHttpSessionIdResolver extends HeaderHttpSessionIdResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();
    protected String headerName;
    protected Integer timeout;
    protected String tokenPrefix;
    protected String authenticateHeaderName;

    public PrefixHeaderHttpSessionIdResolver(String headerName, Integer timeout, String tokenPrefix, String authenticateHeaderName ){
        super(headerName);
        this.headerName = headerName;
        this.timeout=timeout;
        this.tokenPrefix = tokenPrefix.trim();
        this.authenticateHeaderName = authenticateHeaderName;
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        String headerValue = request.getHeader(headerName);
        return (headerValue != null)?Collections.singletonList(headerValue.replace(tokenPrefix+" ", "").trim())
                :Collections.emptyList();
    }

    /**
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param sessionId SpringSessionId
     */
    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response,
                             String sessionId) {
        if(response.getHeader(authenticateHeaderName)==null){
            response.setHeader(this.headerName, tokenPrefix + " " + sessionId);
            if (SecurityConfigConstans.XHR_FIELDS_IS_AJAX_VALUE.equals(request.getHeader(SecurityConfigConstans.XHR_FIELDS_IS_AJAX_KEY))) {
                sessionIdJsonOutput(request, response, sessionId);
            }
        }
    }

    /**
     * セッションIdをJsonで出力
     * Output sessionId using Json.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param sessionId SpringSessionId
     */
    protected void sessionIdJsonOutput(HttpServletRequest request, HttpServletResponse response,
                                       String sessionId){
        try {
            if(request.getRequestURI().equals(SecurityConfigConstans.LOGIN_URL)){
                response.setContentType(APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(new LoginResponseDto(sessionId, tokenPrefix, timeout)));
            }
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
        }
    }
}