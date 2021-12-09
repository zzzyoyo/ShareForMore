package com.example.shareformore.security;

import com.example.shareformore.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;

    // Hint: Now you can view h2-console page at `http://IP-Address:<port>/h2-console` without authentication.
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Make sure we use stateless session; session won't be used to store user's state.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // url 拦截
        http.authorizeRequests()
                .antMatchers("/images/**", "/user/login","/user/register","/api/getCaptcha","/api/showAllPic").permitAll() //不需要认证的服务
                .anyRequest().authenticated();//其他所有的请求都必须登录，认证后才能访问。
        //关闭 csrf 防护
        http.cors().and().csrf().disable();
        //      Here we use JWT(Json Web Token) to authenticate the user.
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }


    //Spring Security 要求：当进行自定义登录逻辑时容器内必须有 PasswordEncoder 实例，所以不能直接 new 对象
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

