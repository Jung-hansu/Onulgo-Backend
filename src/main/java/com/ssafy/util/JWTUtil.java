package com.ssafy.util;

import com.ssafy.users.model.UserDto;
import io.jsonwebtoken.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JWTUtil {


    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access-token.expiretime}")
    private long accessTokenExpireTime;

    @Value("${jwt.refresh-token.expiretime}")
    private long refreshTokenExpireTime;

    private String create(UserDto userDto, String subject, long expireTime){

        Date now = new Date();

        Claims claims = Jwts.claims()
                .setSubject(subject)
                .add("name", userDto.getName())
                .add("user_id", userDto.getUserId())
                .add("email", userDto.getEmail())
                .build();

        String jwt = Jwts.builder()
                .setHeaderParam("typ","JWT")                  //헤더
                .setClaims(claims)
                .setExpiration(new Date(now.getTime() + expireTime))//페이로드
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

        log.debug("JWT 발급 성공 : " + jwt);

        return jwt;

    }

    //AccessToken
    public String createAccessToken(UserDto userDto){
        return create(userDto, "access-token", accessTokenExpireTime);
    }

    //RefreshToken
    public String createRefreshToken(UserDto userDto){
        return create(userDto, "refresh-token", refreshTokenExpireTime);
    }

    public Jws<Claims> checkToken(String token){
        try{
            log.debug("access token {}", token);
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(token);

            log.debug("claims: {}",claims);
            return claims;
        }catch(Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

//    public UserDto decodeToken(String token){
//        try{
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(token);
//
//            log.debug("claims: {}",claims);
//            return true;
//        }catch(Exception e){
//            log.error(e.getMessage());
//            return null;
//        }
//    }
}
