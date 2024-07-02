package com.ssafy.users.model.service;

import com.ssafy.comments.model.CommentDto;
import com.ssafy.plans.model.PlanDto;
import com.ssafy.reviews.model.ReviewDto;
import com.ssafy.users.model.UserDto;
import com.ssafy.users.model.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public void registerUser(UserDto userDto){
        userMapper.registerUser(userDto);
    }

    @Override
    public boolean checkUserEmail(String email){
        return userMapper.checkUserEmail(email);
    }

    @Override
    public UserDto findUserByEmail(String email){
        return userMapper.findUserByEmail(email);
    }

    @Override
    @Transactional
    public void updateUser(UserDto userDto){
        userMapper.updateUser(userDto);
    }

    @Override
    public UserDto getUserInfo(int userId){
        return userMapper.getUserInfo(userId);
    }

    @Override
    @Transactional
    public boolean checkUserPassword(UserDto userDto){
        return userMapper.checkUserPassword(userDto);
    }

    @Override
    @Transactional
    public void deleteUser(int userId){
        userMapper.deleteUser(userId);
    }

    @Override
    public List<PlanDto> getPlans(int userId) {
        return userMapper.getPlans(userId);
    }

    @Override
    public List<PlanDto> getAllDateOfPlans(List<Integer> participantIds) {
        return userMapper.getAllDateOfPlans(participantIds);
    }

    @Override
    public List<ReviewDto> getReviews(int userId) {
        return userMapper.getReviews(userId);
    }

    @Override
    public List<CommentDto> getComments(int userId) { return userMapper.getComments(userId); }

    @Override
    public List<PlanDto> getScrapPlans(int userId) {
        return userMapper.getScrapPlans(userId);
    }

    @Override
    public List<ReviewDto> getScrapReviews(int userId) {
        return userMapper.getScrapReviews(userId);
    }
}
