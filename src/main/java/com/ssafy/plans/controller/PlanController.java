package com.ssafy.plans.controller;

import com.ssafy.plans.model.PlanDto;
import com.ssafy.plans.model.service.PlanService;
import com.ssafy.users.model.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/plans")
@CrossOrigin(origins = "*")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    private void available(String attribute){
        if (attribute == null) throw new NullPointerException();
        if (attribute.isEmpty()) throw new IllegalArgumentException();
    }

    private void available(PlanDto planDto){
        available(planDto.getTitle());
        available(planDto.getStartDate());
        available(planDto.getEndDate());
    }

//    TODO: SearchDto 만들어 페이징 구현하기
    @GetMapping
    public ResponseEntity<?> getPlans(){
        ResponseEntity<?> response;

        try {
            List<PlanDto> plans = planService.getAllPlans();
            response = ResponseEntity.ok(plans);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by getPlans", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/{plan_id}")
    public ResponseEntity<?> getPlan(@PathVariable("plan_id") int planId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("getPlans user: {}", request.getAttribute("user"));
        log.debug("getPlan planId: {}", planId);
        ResponseEntity<?> response;

        try {
            PlanDto plan = planService.getPlan(planId);

            planService.addViewCount(planId);
            plan.setUserId(user.getUserId());
            plan.setJoinUsers(planService.getJoinUsers(planId));
            response = ResponseEntity.ofNullable(plan);

        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by getPlan", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PostMapping
    public ResponseEntity<?> registerPlan(@RequestBody PlanDto planDto, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("registerPlan userDto: {}", user);
        log.debug("registerPlan planDto: {}", planDto);
        ResponseEntity<?> response;

        try {
            available(planDto);
            planDto.setUserId(user.getUserId());
            planService.registerPlan(planDto);
            if(!planDto.getJoinUsers().isEmpty()) {
                planService.inviteToPlan(planDto);
            }
            response = ResponseEntity.created(URI.create("/plans/" + planDto.getPlanId())).body(planDto);
        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by registerPlan", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @DeleteMapping("/{plan_id}")
    public ResponseEntity<?> deletePlan(@PathVariable("plan_id") int planId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("deletePlan userDto: {}", user);
        log.debug("deletePlan planId: {}", planId);
        ResponseEntity<?> response;

        try {
            boolean isOwnPlan = planService.isOwnPlan(
                    PlanDto.builder()
                            .planId(planId)
                            .userId(user.getUserId())
                            .build()
            );

            if (isOwnPlan) {
                planService.deletePlan(
                        PlanDto.builder()
                                .planId(planId)
                                .build()
                );
                response = ResponseEntity.ok().build();
            } else {
                response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by deletePlan", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PutMapping
    public ResponseEntity<?> modifyPlan(@RequestBody PlanDto planDto, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("modifyPlan user: {}", user);
        log.debug("modifyPlan planDto: {}", planDto);
        ResponseEntity<?> response;

        try {
            boolean isOwnPlan = planService.isOwnPlan(
                    PlanDto.builder()
                            .planId(planDto.getPlanId())
                            .userId(user.getUserId())
                            .build()
            );

            if (isOwnPlan) {
                planService.modifyPlan(planDto);
                response = ResponseEntity.ok().build();
            } else {
                response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by modifyPlan", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/{plan_id}/join")
    public ResponseEntity<?> getParticipants(@PathVariable("plan_id") int planId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("inviteToPlan user: {}", user);
        log.debug("inviteToPlan planId: {}", planId);
        ResponseEntity<?> response;
        Map<String, Object> map = new HashMap<>();

        try {
            boolean isOwnPlan = planService.isOwnPlan(
                    PlanDto.builder()
                            .planId(planId)
                            .userId(user.getUserId())
                            .build()
            );

            if (isOwnPlan) {
                map.put("isOwn", true);
                map.put("participants", planService.getParticipants(planId));
            } else{
                map.put("isOwn", false);
                map.put("participants", planService.getNumOfParticipants(planId));
            }
            response = ResponseEntity.ok(map);
        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by inviteToPlan", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PutMapping("/{plan_id}/join")
    public ResponseEntity<?> modifyParticipants(@RequestBody Map<String, List<UserDto>> map, @PathVariable("plan_id") int planId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("expelFromPlan userIds: {}", user);
        log.debug("expelFromPlan userChanging: {}", map);
        ResponseEntity<?> response;

        try {
            boolean isOwnPlan = planService.isOwnPlan(
                    PlanDto.builder()
                            .planId(planId)
                            .userId(user.getUserId())
                            .build()
            );

            if (isOwnPlan) {
                List<UserDto> added = map.get("added");
                List<UserDto> removed = map.get("removed");

                if (!added.isEmpty()){
                    planService.inviteToPlan(
                            PlanDto.builder()
                                    .planId(planId)
                                    .joinUsers(added)
                                    .build()
                    );
                }
                if (!removed.isEmpty()) {
                    planService.expelFromPlan(
                            PlanDto.builder()
                                    .planId(planId)
                                    .joinUsers(removed)
                                    .build()
                    );
                }
                response = ResponseEntity.ok().build();
            } else {
                response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by expelFromPlan", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/join")
    public ResponseEntity<?> getJoinedPlans(HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("getJoinedPlans userIds: {}", user);
        ResponseEntity<?> response;

        try {
            response = ResponseEntity.ok(planService.getJoinedPlans(user.getUserId()));
        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by getJoinedPlans", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PutMapping("/{plan_id}/like")
    public ResponseEntity<?> updateLike(@PathVariable("plan_id") int planId, @RequestBody boolean isChecked, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("updateLike userIds: {}", user);
        ResponseEntity<?> response;

        try {
            PlanDto plan = PlanDto.builder()
                    .userId(user.getUserId())
                    .planId(planId)
                    .build();

            if (isChecked) {
                planService.doLike(plan);
            } else {
                planService.undoLike(plan);
            }
            response = ResponseEntity.ok().build();
        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by updateLike", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PutMapping("/{plan_id}/scrap")
    public ResponseEntity<?> updateScrap(@PathVariable("plan_id") int planId, @RequestBody boolean isChecked, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("updateScrap userIds: {}", user);
        ResponseEntity<?> response;

        try {
            PlanDto plan = PlanDto.builder()
                    .userId(user.getUserId())
                    .planId(planId)
                    .build();

            if (isChecked) {
                planService.doScrap(plan);
            } else {
                planService.undoScrap(plan);
            }
            response = ResponseEntity.ok().build();
        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by updateScrap", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/scrap")
    public ResponseEntity<?> getScrapPlans(HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");

        log.debug("getScrapPlans userIds: {}", user);

        ResponseEntity<?> response;
        try{
            List<PlanDto>plans = planService.getScrapPlans(user.getUserId());
            response = ResponseEntity.ofNullable(plans);
        }catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by updateScrap", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }
}
