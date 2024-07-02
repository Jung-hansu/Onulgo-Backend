package com.ssafy.users.model.mapper;

import com.ssafy.comments.model.CommentDto;
import com.ssafy.plans.model.PlanDto;
import com.ssafy.reviews.model.ReviewDto;
import com.ssafy.users.model.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    void registerUser(UserDto userDto);
    boolean checkUserEmail(String email);
    UserDto findUserByEmail(String email);
    void updateUser(UserDto userDto);
    UserDto getUserInfo(int userId);
    boolean checkUserPassword(UserDto userDto);
    void deleteUser(int userId);
    List<PlanDto> getPlans(int userId);
    List<PlanDto> getAllDateOfPlans(List<Integer> participantIds);
    List<ReviewDto> getReviews(int userId);
    List<CommentDto> getComments(int userId);
    List<PlanDto> getScrapPlans(int userId);
    List<ReviewDto> getScrapReviews(int userId);
}
