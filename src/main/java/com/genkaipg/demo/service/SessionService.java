package com.genkaipg.demo.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;

@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionService<SessionDto> implements Serializable {

    private SessionDto data;

    public SessionDto getData() {
        return data;
    }

    public void setData(SessionDto data) {
        this.data = data;
    }
}
