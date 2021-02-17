package com.genkaipg.demo;

import com.genkaipg.demo.filter.PrefixHeaderHttpSessionIdResolver;
import com.genkaipg.demo.constants.SecurityConfigConstans;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionIdResolver;

@EnableRedisHttpSession(maxInactiveIntervalInSeconds = SecurityConfigConstans.REDIS_SESSION_TIMEOUT)
@Configuration
public class SessionConfig {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private Integer redisPort;

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        //return HeaderHttpSessionIdResolver.xAuthToken();
        return new PrefixHeaderHttpSessionIdResolver(SecurityConfigConstans.AUTHORIZATION_HEADER, SecurityConfigConstans.REDIS_SESSION_TIMEOUT, SecurityConfigConstans.TOKEN_PREFIX, SecurityConfigConstans.AUTHENTICATE_HEADER);
    }

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost,redisPort));
    }

}
