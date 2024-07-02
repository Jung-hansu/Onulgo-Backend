package com.ssafy.plans.model.service;

import com.ssafy.plans.model.PlanDto;
import com.ssafy.plans.model.mapper.PlanMapper;
import com.ssafy.reviews.model.ReviewDto;
import com.ssafy.users.model.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PlanServiceImpl implements PlanService{

    private final PlanMapper planMapper;

    public PlanServiceImpl(PlanMapper planMapper) {
        this.planMapper = planMapper;
    }

    @Override
    public List<PlanDto> getAllPlans() {
        return planMapper.getAllPlans();
    }

    @Override
    public PlanDto getPlan(int planId) {
        return planMapper.getPlan(planId);
    }

    @Override
    @Transactional
    public void registerPlan(PlanDto planDto) {
        planMapper.registerPlan(planDto);
    }

    @Override
    @Transactional
    public void deletePlan(PlanDto planDto) {
        planMapper.deletePlan(planDto);
    }

    @Override
    @Transactional
    public void modifyPlan(PlanDto planDto) {
        planMapper.modifyPlan(planDto);
    }

    @Override
    @Transactional
    public void inviteToPlan(PlanDto planDto) {
        planMapper.inviteToPlan(planDto);
    }

    @Override
    @Transactional
    public void expelFromPlan(PlanDto planDto) {
        planMapper.expelFromPlan(planDto);
    }

    @Override
    public List<UserDto> getParticipants(int planId) {
        return planMapper.getParticipants(planId);
    }

    @Override
    public int getNumOfParticipants(int planId) {
        return planMapper.getNumOfParticipants(planId);
    }

    @Override
    public List<PlanDto> getJoinedPlans(int userId) {
        return planMapper.getJoinedPlans(userId);
    }

    @Override
    public List<UserDto> getJoinUsers(int planId){
        return planMapper.getJoinUsers(planId);
    }

    @Override
    public boolean isOwnPlan(PlanDto planDto) {
        return planMapper.isOwnPlan(planDto);
    }

    @Override
    @Transactional
    public void doLike(PlanDto planDto) {
        planMapper.doLike(planDto);
    }

    @Override
    @Transactional
    public void undoLike(PlanDto planDto) {
        planMapper.undoLike(planDto);
    }

    @Override
    @Transactional
    public void doScrap(PlanDto planDto) {
        planMapper.doScrap(planDto);
    }

    @Override
    @Transactional
    public void undoScrap(PlanDto planDto) {
        planMapper.undoScrap(planDto);
    }

    @Override
    public void addViewCount(int planId) {
        planMapper.addViewCount(planId);
    }

    @Override
    public List<PlanDto> getScrapPlans(int userId) {
        return planMapper.getScrapPlans(userId);
    }
}
