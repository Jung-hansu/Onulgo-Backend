package com.ssafy.auth.model.service;

import com.ssafy.users.model.TokenDto;
import com.ssafy.users.model.UserDto;

import java.sql.SQLException;

public interface AuthService {
    UserDto login(UserDto userDto)throws SQLException;
    void logout(int userId)throws SQLException;
    void updateRefreshToken(TokenDto tokenDto)throws SQLException;
    String findEmail(UserDto userDto)throws SQLException;
    String findPassword(UserDto userDto)throws SQLException;
    int checkEmail(String email) throws SQLException;
    UserDto checkRefreshToken(String refreshToken) throws SQLException;
}
