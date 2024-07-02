package com.ssafy.auth.model.service;

import com.ssafy.auth.model.mapper.AuthMapper;
import com.ssafy.users.model.TokenDto;
import com.ssafy.users.model.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthMapper authMapper;

    public AuthServiceImpl(AuthMapper authMapper) {
        this.authMapper = authMapper;
    }

    @Override
    public UserDto login(UserDto userDto) throws SQLException {
        UserDto user = authMapper.login(userDto);
        if(user!=null){
            authMapper.updateLogin(user.getUserId());
            return user;
        }
        return null;
    }

    @Override
    public void logout(int userId) throws SQLException {
        authMapper.logout(userId);
    }

    @Override
    public void updateRefreshToken(TokenDto tokenDto) throws SQLException {
        authMapper.updateRefreshToken(tokenDto);
    }

    @Override
    public String findEmail(UserDto userDto)throws SQLException {
        return authMapper.findEmail(userDto);
    }

    @Override
    public String findPassword(UserDto userDto)throws SQLException {
        return authMapper.findPassword(userDto);
    }

    @Override
    public int checkEmail(String email) throws SQLException {
        return authMapper.checkEmail(email);
    }

    @Override
    public UserDto checkRefreshToken(String refreshToken) throws SQLException {
        return authMapper.checkRefreshToken(refreshToken);
    }
}
