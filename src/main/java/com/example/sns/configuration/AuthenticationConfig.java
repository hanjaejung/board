package com.example.sns.configuration;

import com.example.sns.configuration.filter.JwtTokenFilter;
import com.example.sns.exception.CustomAuthenticationEntryPoint;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().regexMatchers("^(?!/api/).*") // /api로 시작하는것만 통과(프론트에 있는 url이 자꾸걸려 추가)
                .antMatchers(HttpMethod.POST, "/api/*/users/join", "/api/*/users/login");
        //홈페이지에 접근 가능하게 해준다
        //DefaultLoginPageGenerationFilter

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
               // .antMatchers("/api/*/users/join", "/api/*/users/login").permitAll() //여기 경로는 모두 허용
                .antMatchers("/api/**").authenticated() //여기서 선택된 경로는 header의 jwt토큰인증을 받아야 한다
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtTokenFilter(secretKey, userService), UsernamePasswordAuthenticationFilter.class);


        //addFilterBefore(A, B) B이전에 A를 적용해라

    }
}
