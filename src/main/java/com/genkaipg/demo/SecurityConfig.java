package com.genkaipg.demo;


import com.genkaipg.demo.filter.RestAuthenticationEntryPoint;
import com.genkaipg.demo.filter.handler.RestAccessDeniedHandler;
import com.genkaipg.demo.filter.handler.RestAuthenticationFailureHandler;
import com.genkaipg.demo.filter.handler.RestLogoutSuccessHandler;
import com.genkaipg.demo.constants.SecurityConfigConstans;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final DataSource dataSource;
    public SecurityConfig(DataSource dataSource) {
        this.dataSource=dataSource;
    }

    //Webベースのセキュリティを設定
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //リクエスト承認ロジック
                .authorizeRequests()
                    //マッチしたアドレスはロールチェックで許可
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/user/**").hasRole("USER")
                    .antMatchers("/all/**").permitAll()
                    //全ての任意のリクエストは認証済みで許可
                    .anyRequest().authenticated()
                //デフォログインロジック 全員許可
                .and().formLogin().permitAll()
                    //成功
                    .successHandler((req,res,auth)->{/*リダイレクト無効化 response処理はSpringSessionへ*/})
                    //失敗
                    .failureHandler(new RestAuthenticationFailureHandler(SecurityConfigConstans.TOKEN_PREFIX, SecurityConfigConstans.AUTHENTICATE_HEADER))
                //認証失敗 ロジック
                .and().exceptionHandling()
                    //トークン認証失敗
                    .authenticationEntryPoint(new RestAuthenticationEntryPoint(SecurityConfigConstans.TOKEN_PREFIX, SecurityConfigConstans.AUTHENTICATE_HEADER, SecurityConfigConstans.LOGIN_URL ))
                    //antMatchersでロールなし
                    .accessDeniedHandler(new RestAccessDeniedHandler(SecurityConfigConstans.TOKEN_PREFIX, SecurityConfigConstans.AUTHENTICATE_HEADER))
                //デフォログアウトロジック 全員許可
                .and().logout().permitAll()
                    //ログアウト
                    .logoutSuccessHandler((new RestLogoutSuccessHandler(SecurityConfigConstans.TOKEN_PREFIX, SecurityConfigConstans.AUTHORIZATION_HEADER)))
                //Tokenなので不要
                //.and().cors()
                //csrfはdisable
                .and().csrf().disable();
    }

    //認証サービス登録
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //user情報ID/PASS/ROLE提供サービス
    @Bean
    public UserDetailsService userDetailsService(){
        JdbcDaoImpl jdbcDaoImpl = new JdbcDaoImpl();
        jdbcDaoImpl.setEnableGroups(true);
        jdbcDaoImpl.setDataSource(dataSource);
        return jdbcDaoImpl;
    }
}
