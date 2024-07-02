package com.ssafy.interceptor;

import com.ssafy.util.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ssafy.users.model.UserDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.net.HttpCookie;
import java.util.Arrays;

@Slf4j
@Component
public class ConfirmInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;

    public ConfirmInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    //TODO : 액세스 토큰 만료 시 리프레시 토큰으로 재발급
    //TODO : 리프레시 토큰 만료 시 로그인 화면으로 강제 이동
    //JWT 검증하기
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {



        String token = (String)request.getHeader("Authorization");
        log.debug("token: {}", token);


        if(token != null && token.startsWith("Bearer ")) {

            String accessToken = (String) token.split(" ")[1];
            log.debug("accessToken: {}", accessToken);
            log.debug("accessToken is null: {}", accessToken == null);

            Jws<Claims> claims = jwtUtil.checkToken(accessToken);
            log.debug("JWT Check : {}", claims);

            log.debug("name {}", claims.getPayload().get("name"));
            log.debug("user_id {}", claims.getPayload().get("user_id"));
            log.debug("email {}", claims.getPayload().get("email"));


            request.setAttribute("user",
                    UserDto.builder()
                            .name((String) claims.getPayload().get("name"))
                            .userId((Integer) claims.getPayload().get("user_id"))
                            .email((String) claims.getPayload().get("email"))
                            .build()
            );

            log.debug("JWT payload : {}", request.getAttribute("user"));

            return true;
        }


        log.debug("No JWT Token {}","false");

        return false;
    }
}