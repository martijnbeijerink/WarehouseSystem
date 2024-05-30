package com.example.warehouse.StockManagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // disable CSRF for simplicity, not recommended for production
                .authorizeRequests()
                .antMatchers("/api/skus/**").authenticated()
                .and()
                .httpBasic();  // Use Basic Authentication
    }
}
