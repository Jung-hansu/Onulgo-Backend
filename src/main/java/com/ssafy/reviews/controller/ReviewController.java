package com.ssafy.reviews.controller;

import com.ssafy.comments.model.CommentDto;
import com.ssafy.comments.model.CommentLikeDto;
import com.ssafy.comments.model.service.CommentService;
import com.ssafy.plans.model.PlanDto;
import com.ssafy.reviews.model.ReviewDto;
import com.ssafy.reviews.model.ReviewImageDto;
import com.ssafy.reviews.model.ReviewLikeDto;
import com.ssafy.reviews.model.ReviewScrapDto;
import com.ssafy.reviews.model.service.ReviewService;
import com.ssafy.users.model.UserDto;
import com.ssafy.util.JWTUtil;
import com.ssafy.util.S3FileUploadUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.events.Comment;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;
    private final CommentService commentService;
    private final JWTUtil jwtUtil;
    private final S3FileUploadUtil s3FileUploadUtil;

    //TODO: user 아이디 따로 받아야 함
    @GetMapping(value = {"", "/"})
    public ResponseEntity<?> getAllReviews(HttpServletRequest request){

        log.debug("getAllReviews");
        ResponseEntity<?> response;

        try {

            int userId = 0;

            String token = (String)request.getHeader("Authorization");

            if(token!=null && token.startsWith("Bearer ")){
                String accessToken = (String) token.split(" ")[1];

                Jws<Claims> claims = jwtUtil.checkToken(accessToken);

                UserDto user = UserDto.builder()
                        .name((String)claims.getPayload().get("name"))
                        .userId((Integer)claims.getPayload().get("user_id"))
                        .email((String)claims.getPayload().get("email"))
                        .build();

                userId = user.getUserId();

                log.debug("modifyReview user: {}", user);
            }

            List<ReviewDto> reviews = reviewService.getAllReviews(userId);
            response = ResponseEntity.ofNullable(reviews);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by getAllReviews", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/detail/{review_id}")
    public ResponseEntity<?> getReview(@PathVariable("review_id") int reviewId, HttpServletRequest request){
        log.debug("getReview reviewId: {}", reviewId);
        ResponseEntity<?> response;

        try {

            int userId = 0;
            String token = (String)request.getHeader("Authorization");

            if(token!=null && token.startsWith("Bearer ")){
                String accessToken = (String) token.split(" ")[1];

                Jws<Claims> claims = jwtUtil.checkToken(accessToken);

                UserDto user = UserDto.builder()
                        .name((String)claims.getPayload().get("name"))
                        .userId((Integer)claims.getPayload().get("user_id"))
                        .email((String)claims.getPayload().get("email"))
                        .build();

                userId = user.getUserId();

                log.debug("modifyReview user: {}", user);
            }

            reviewService.updateViewCount(reviewId);
            log.debug("userId : {}",userId);
            ReviewDto review = reviewService.getReview(ReviewLikeDto.builder()
                    .reviewId(reviewId)
                    .userId(userId)
                    .build()
            );
            review.setComments(commentService.getComments(CommentLikeDto.builder()
                    .reviewId(reviewId)
                    .userId(userId)
                    .build()
            ));
            review.setImages(reviewService.getReviewImages(reviewId));
            response = ResponseEntity.ofNullable(review);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by getReview", e);
            response = ResponseEntity.badRequest().build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @GetMapping("/{plan_id}")
    public ResponseEntity<?> getReviewsOfPlan(@PathVariable("plan_id") int planId){
        log.debug("getReviewsOfPlan planId: {}", planId);
        ResponseEntity<?> response;

        try {
            List<ReviewDto> reviews = reviewService.getReviewsOfPlan(planId);
            response = ResponseEntity.ofNullable(reviews);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by getReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

//    TODO: images 저장 로직 짜기
    @PostMapping("/{plan_id}")
    public ResponseEntity<?> registerReview(@PathVariable("plan_id") int planId, @RequestBody ReviewDto reviewDto, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("registerReview user: {}", user);
        log.debug("registerReview planId: {}", planId);
        log.debug("registerReview reviewDto: {}", reviewDto);
        ResponseEntity<?> response;

        reviewDto.setPlanId(planId);
        reviewDto.setUserId(user.getUserId());

        try {
            reviewService.registerReview(reviewDto);

            log.debug("created ReviewId : {}",reviewDto.getReviewId() );
            response = ResponseEntity.created(URI.create("/reviews/detail/" + reviewDto.getReviewId())).body(reviewDto);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by registerReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PostMapping("/{review_id}/image")
    public ResponseEntity<?> registerReviewImages(@PathVariable("review_id") int reviewId, @RequestParam("review_images") List<MultipartFile> images){
        log.debug("registerReview reviewId: {}", reviewId);
        ResponseEntity<?> response;

        try {
            reviewService.registerReviewImages(reviewId, images);
            response = ResponseEntity.created(URI.create("/reviews/detail/" + reviewId)).build();
        } catch (IllegalArgumentException | NullPointerException | IOException e) {
            log.error("Exception caused by registerReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @DeleteMapping("/{review_id}/image")
    public ResponseEntity<?> deleteReviewImages(@PathVariable("review_id") int reviewId, @RequestBody List<ReviewImageDto> images){
        log.debug("deleteReviewImages reviewId: {}", reviewId);
        ResponseEntity<?> response;

        try {
            System.out.println(images);
            reviewService.deleteReviewImages(reviewId, images);
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException | IOException e) {
            log.error("Exception caused by registerReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PutMapping(value={"", "/"})
    public ResponseEntity<?> modifyReview(@RequestBody ReviewDto reviewDto, HttpServletRequest request){
        String token = (String)request.getHeader("Authorization");
        String accessToken = (String) token.split(" ")[1];

        Jws<Claims> claims = jwtUtil.checkToken(accessToken);

        UserDto user = UserDto.builder()
                .name((String)claims.getPayload().get("name"))
                .userId((Integer)claims.getPayload().get("user_id"))
                .email((String)claims.getPayload().get("email"))
                .build();

        log.debug("modifyReview user: {}", user);
        log.debug("modifyReview reviewDto: {}", reviewDto);
        ResponseEntity<?> response;

        try {
            reviewService.modifyReview(
                    ReviewDto.builder()
                            .reviewId(reviewDto.getReviewId())
                            .userId(user.getUserId())
                            .title(reviewDto.getTitle())
                            .content(reviewDto.getContent())
                            .build()
            );
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by modifyReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @DeleteMapping("/{review_id}")
    public ResponseEntity<?> deleteReview(@PathVariable("review_id") int reviewId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("deleteReview user: {}", user);
        log.debug("deleteReview reviewId: {}", reviewId);
        ResponseEntity<?> response;

        try {
            reviewService.deleteReview(
                    ReviewDto.builder()
                            .reviewId(reviewId)
                            .userId(user.getUserId())
                            .build()
            );
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by deleteReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PutMapping("/comment/{review_id}")
    public ResponseEntity<?> updateCommentCount(@PathVariable("review_id") int reviewId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");

        log.debug("commentCount: {}", reviewId);
        ResponseEntity<?> response;

        try {
            reviewService.updateCommentCount(reviewId);
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by deleteReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PutMapping("/like/{review_id}")
    public ResponseEntity<?> updateLikeCount(@PathVariable("review_id") int reviewId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("likeCount: {}", reviewId);

        ResponseEntity<?> response;

        try {
            reviewService.updateLikeCount(reviewId);
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by deleteReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PutMapping("/scrap/{review_id}")
    public ResponseEntity<?> updateScrapCount(@PathVariable("review_id") int reviewId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("scrapCount: {}", reviewId);

        ResponseEntity<?> response;

        try {
            reviewService.updateScrapCount(reviewId);
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by deleteReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PostMapping("/like/{review_id}")
    public ResponseEntity<?> addLike(@PathVariable("review_id") int reviewId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("addLike: {}", reviewId);

        ResponseEntity<?> response;

        try {
            reviewService.addLike(ReviewLikeDto.builder()
                    .reviewId(reviewId)
                    .userId(user.getUserId())
                    .build());
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by deleteReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @DeleteMapping("/like/{review_id}")
    public ResponseEntity<?> deleteLike(@PathVariable("review_id") int reviewId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("deleteLike: {}", reviewId);

        ResponseEntity<?> response;

        try {
            reviewService.deleteLike(ReviewLikeDto.builder()
                    .reviewId(reviewId)
                    .userId(user.getUserId())
                    .build());
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by deleteReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PostMapping("/scrap/{review_id}")
    public ResponseEntity<?> addScrap(@PathVariable("review_id") int reviewId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("addScrap: {}", reviewId);

        ResponseEntity<?> response;

        try {
            reviewService.addScrap(ReviewScrapDto.builder()
                    .reviewId(reviewId)
                    .userId(user.getUserId())
                    .build());
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by deleteReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @DeleteMapping("/scrap/{review_id}")
    public ResponseEntity<?> deleteScrap(@PathVariable("review_id") int reviewId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("deleteScrap: {}", reviewId);

        ResponseEntity<?> response;

        try {
            reviewService.deleteScrap(ReviewScrapDto.builder()
                    .reviewId(reviewId)
                    .userId(user.getUserId())
                    .build());
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by deleteReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularReviews(HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("popularReviews");

        ResponseEntity<?> response;

        try {
            List<ReviewDto> popularReviewList = reviewService.getPopularReviews();

            for (ReviewDto review : popularReviewList) {
                review.setImages(reviewService.getReviewImages(review.getReviewId()));
            }
            response = ResponseEntity.ofNullable(popularReviewList);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by deleteReview", e);
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
            List<ReviewDto>plans = reviewService.getScrapReviews(user.getUserId());
            response = ResponseEntity.ofNullable(plans);
        }catch(IllegalArgumentException | NullPointerException e){
            log.error("Exception caused by updateScrap", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }
}
