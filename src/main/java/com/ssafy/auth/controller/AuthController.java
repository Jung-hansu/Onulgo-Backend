package com.ssafy.auth.controller;

import com.ssafy.auth.model.service.AuthService;
import com.ssafy.users.model.TokenDto;
import com.ssafy.users.model.UserDto;
import com.ssafy.util.EncryptUtil;
import com.ssafy.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
public class AuthController {

    private final AuthService authService;
    private final JWTUtil jwtUtil;
    private final EncryptUtil encryptUtil;

    public AuthController(AuthService authService, JWTUtil jwtUtil, EncryptUtil encryptUtil) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.encryptUtil = encryptUtil;
    }

    //TODO: 비밀번호 암호화 기능 구현
    //TODO: response Header 역할 확인 후 주석 제거
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto, HttpServletResponse response) {
        Map<String,String> JWT = new HashMap<>();
        String accessToken = null;
        String refreshToken = null;

//        encryptUtil.
        try {

            UserDto user = authService.login(userDto);

            accessToken = jwtUtil.createAccessToken(
                    UserDto.builder()
                            .userId(user.getUserId())
                            .email(user.getEmail())
                            .name(user.getName())
                            .build()
            );

            refreshToken = jwtUtil.createRefreshToken(
                    UserDto.builder()
                            .userId(user.getUserId())
                            .email(user.getEmail())
                            .name(user.getName())
                            .build()
            );

            JWT.put("access_token", accessToken);
            JWT.put("refresh_token", refreshToken);

            Cookie accessTokenCookie = new Cookie("access_token", accessToken);
            accessTokenCookie.setMaxAge(60*60);
            accessTokenCookie.setPath("/");

            Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
            refreshTokenCookie.setMaxAge(60*60*24*7);
            refreshTokenCookie.setPath("/");


            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);

            authService.updateRefreshToken(
                    TokenDto.builder()
                            .userId(user.getUserId())
                            .refreshToken(refreshToken)
                            .build()
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(JWT);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        UserDto user = (UserDto)request.getAttribute("user");

        Cookie cookie = new Cookie("access_token", null);
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        try {
            authService.logout(user.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(true);
    }

    //TODO: 이메일 확인 절차 구체화
    @PostMapping("/find/email")
    public ResponseEntity<?> findEmail(@RequestBody UserDto userDto) {
        Map<String,String>res = new HashMap<>();
        String email = null;
        try {
            email = authService.findEmail(userDto);
            res.put("email", email);
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(res);
    }

    //TODO: 비밀번호 암호화 기능 구현
    //TODO: 임시 비밀번호 생성 기능 구현
    @PostMapping("/find/password")
    public ResponseEntity<?> findPassword(@RequestBody UserDto userDto) {
        Map<String, String>res = new HashMap<>();
        String password = null;
        try {
            password = authService.findPassword(userDto);
            res.put("password",password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(HttpServletRequest request) {

        UserDto user = (UserDto)request.getAttribute("user");
        log.debug("user: {}",user);
        if(user==null){
            return ResponseEntity.badRequest().build();
        }


        Map<String, UserDto>res = new HashMap<>();
        res.put("user",user);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String accessToken = null;
        UserDto user = null;
        Map<String,String> res = new HashMap<>();

        if(token==null || !token.startsWith("Bearer ")){
            String refreshToken = token.split(" ")[1];

            //리프레시 토큰 기반으로 사용자 정보 가져오기
            try {
                user = authService.checkRefreshToken(refreshToken);
                log.debug("user : {}", user);

                //해당 리프레시 토큰을 가진 유저가 존재하는 경우
                if(user != null){
                    accessToken = jwtUtil.createAccessToken(user);
                    res.put("access_token",accessToken);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.ok(res);
    }

    @GetMapping("/check/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable String email) {

        int cnt = 0;

        try{
            cnt = authService.checkEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(cnt == 0);
    }
}
