package com.ssafy.auth.model.mapper;

import com.ssafy.users.model.TokenDto;
import com.ssafy.users.model.UserDto;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;

@Mapper
public interface AuthMapper {

    UserDto login(UserDto userDto) throws SQLException;
    void logout(int userId) throws SQLException;
    String findEmail(UserDto userDto) throws SQLException;
    String findPassword(UserDto userDto) throws SQLException;
    void updateRefreshToken(TokenDto tokenDto)throws SQLException;
    void updateLogin(int userId)throws SQLException;
    int checkEmail(String email) throws SQLException;
    UserDto checkRefreshToken(String refreshToken) throws SQLException;
}
