package com.ssafy.users.controller;

import com.ssafy.comments.model.CommentDto;
import com.ssafy.plans.model.PlanDto;
import com.ssafy.plans.model.service.PlanService;
import com.ssafy.reviews.model.ReviewDto;
import com.ssafy.users.model.UserDto;
import com.ssafy.users.model.service.UserService;
import com.ssafy.util.EncryptUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final PlanService planService;
    private final EncryptUtil encryptUtil;

    private void available(String attribute){
        if (attribute == null) throw new NullPointerException();
        if (attribute.isEmpty()) throw new IllegalArgumentException();
    }

    private void available(UserDto userDto){
        available(userDto.getEmail());
        available(userDto.getPassword());
        available(userDto.getName());
        available(userDto.getAddress());
        available(userDto.getPhoneNumber());
    }

    //TODO: 회원가입 시 tokens 테이블에 공간 추가
    //TODO: 회원가입 시 비밀번호 암호화를 위한 Salt 생성 및 저장
    @PostMapping(value = {"", "/"})
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
        log.debug("registerUser user: {}", userDto);
        ResponseEntity<?> response;

        try {
            available(userDto);

            String salt = encryptUtil.generateSalt();
            System.out.println(salt);
            userService.registerUser(
                    UserDto.builder()
                            .name(userDto.getName())
                            .userId(userDto.getUserId())
                            .address(userDto.getAddress())
                            .email(userDto.getEmail())
                            .phoneNumber(userDto.getPhoneNumber())
//                            .password(EncryptUtil.getEncrypt(userDto.getPassword(),salt))
                            .password(userDto.getPassword())
                            .salt(salt)
                            .build()
            );
            response = ResponseEntity.created(URI.create("/users/" + userDto.getUserId())).build();
        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by registerUser", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/validate/{email}")
    public ResponseEntity<?> checkUserEmail(@PathVariable("email") String email){
        log.debug("checkUserEmail email: {}", email);
        ResponseEntity<?> response;

        try {
            Map<String, Boolean> map = new HashMap<>();
            available(email);
            map.put("flag", userService.checkUserEmail(email));
            System.out.println(map.get("flag"));
            response = ResponseEntity.ok(map);
        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by registerUser", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/find/{email}")
    public ResponseEntity<?> findUserByEmail(@PathVariable("email") String email){
        log.debug("findUserByEmail email: {}", email);
        ResponseEntity<?> response;

        try {
            available(email);
            response = ResponseEntity.ok(userService.findUserByEmail(email));
        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by registerUser", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }


    @PutMapping(value = {"", "/"})
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("updateUser user: {}", userDto);
        ResponseEntity<?> response;

        try {
            available(userDto);
            if (user.getUserId() == userDto.getUserId()) {
                userService.updateUser(userDto);
                response = ResponseEntity.ok().build();
            } else {
                response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by registerUser", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserInfo(@PathVariable("user_id") int userId, HttpServletRequest request) {
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("getUserInfo userId: {}", userId);
        ResponseEntity<?> response;

        try {
            if (user.getUserId() == userId) {
                UserDto userDto = userService.getUserInfo(userId);
                response = ResponseEntity.ofNullable(userDto);
            } else {
                response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by getUserInfo", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("getUserInfo userId: {}", user.getUserId());
        ResponseEntity<?> response;

        try {
            UserDto userDto = userService.getUserInfo(user.getUserId());
            response = ResponseEntity.ofNullable(userDto);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by getUserInfo", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PostMapping("/check")
    public ResponseEntity<?> checkUserPassword(@RequestBody UserDto userDto, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("checkUserPassword userDto: {}", userDto);
        ResponseEntity<?> response;

        try {
            available(userDto.getPassword());
            Map<String, Object> map = new HashMap<>();
            boolean flag = userService.checkUserPassword(
                    UserDto.builder()
                            .userId(user.getUserId())
                            .password(userDto.getPassword())
                            .build()
            );

            map.put("flag", flag);
            response = ResponseEntity.ok(map);
        } catch (IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by checkUserPassword", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable("user_id") int userId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("deleteUser userId: {}", userId);
        ResponseEntity<?> response;

        try {
            if (user.getUserId() == userId) {
                userService.deleteUser(userId);
                response = ResponseEntity.ok().build();
            } else {
                response = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by deleteUser", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/plans")
    public ResponseEntity<?> getPlans(HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("getPlans user: {}", user);
        ResponseEntity<?> response;

        try {
            List<PlanDto> plans = userService.getPlans(user.getUserId());
            for (PlanDto plan : plans){
                plan.setJoinUsers(planService.getJoinUsers(plan.getPlanId()));
            }
            response = ResponseEntity.ofNullable(plans);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by getPlans", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/plans/dates")
    public ResponseEntity<?> getAllDateOfPlans(@RequestParam String participants, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("getAllDateOfPlans user: {}", user);
        log.debug("getAllDateOfPlans participants: {}", participants);
        ResponseEntity<?> response;

        try {
            System.out.println("sdjafhaklghadljkghwoa;gklj;er");
            List<Integer> participantIds = Arrays.stream(participants.split(","))
                    .map(Integer::parseInt)
                    .toList();

            response = ResponseEntity.ok(userService.getAllDateOfPlans(participantIds));
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by getPlans", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> getReviews(HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("getReviews user: {}", user);
        ResponseEntity<?> response;

        try {
            List<ReviewDto> reviews = userService.getReviews(user.getUserId());
            response = ResponseEntity.ofNullable(reviews);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by getPlans", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getComments(HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("getComments user: {}", user);
        ResponseEntity<?> response;

        try {
            List<CommentDto> comments = userService.getComments(user.getUserId());
            response = ResponseEntity.ofNullable(comments);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by getPlans", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }
    @GetMapping("/scrap/plans")
    public ResponseEntity<?> getScrapPlans(HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("getScrapPlans user: {}", user);
        ResponseEntity<?> response;

        try {
            List<PlanDto> plans = userService.getScrapPlans(user.getUserId());
            response = ResponseEntity.ofNullable(plans);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by getPlans", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/scrap/reviews")
    public ResponseEntity<?> getScrapReviews(HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("getScrapReviews user: {}", user);
        ResponseEntity<?> response;

        try {
            List<ReviewDto> reviews = userService.getScrapReviews(user.getUserId());
            response = ResponseEntity.ofNullable(reviews);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by getPlans", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }
}
