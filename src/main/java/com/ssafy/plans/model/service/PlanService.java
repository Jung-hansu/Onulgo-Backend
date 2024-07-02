package com.ssafy.plans.model.service;

import com.ssafy.plans.model.PlanDto;
import com.ssafy.reviews.model.ReviewDto;
import com.ssafy.users.model.UserDto;

import java.util.List;

public interface PlanService {

    List<PlanDto> getAllPlans();
    PlanDto getPlan(int planId);
    void registerPlan(PlanDto planDto);
    void deletePlan(PlanDto planDto);
    void modifyPlan(PlanDto planDto);
    List<UserDto> getParticipants(int planId);
    int getNumOfParticipants(int planId);
    void inviteToPlan(PlanDto planDto);
    void expelFromPlan(PlanDto planDto);
    List<PlanDto> getJoinedPlans(int userId);
    List<UserDto> getJoinUsers(int planId);
    boolean isOwnPlan(PlanDto planDto);

    void doLike(PlanDto planDto);
    void undoLike(PlanDto planDto);
    void doScrap(PlanDto planDto);
    void undoScrap(PlanDto planDto);
    void addViewCount(int planId);
    List<PlanDto>getScrapPlans(int userId);
}
