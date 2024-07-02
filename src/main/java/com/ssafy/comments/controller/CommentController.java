package com.ssafy.comments.controller;

import com.ssafy.comments.model.CommentDto;
import com.ssafy.comments.model.CommentLikeDto;
import com.ssafy.comments.model.service.CommentService;
import com.ssafy.reviews.model.ReviewLikeDto;
import com.ssafy.users.model.UserDto;
import com.ssafy.util.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final JWTUtil jwtUtil;

    public CommentController(CommentService commentService, JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
        this.commentService = commentService;
    }

    @GetMapping("/{review_id}")
    public ResponseEntity<?> getComments(@PathVariable("review_id") int reviewId, HttpServletRequest request){
        ResponseEntity<?> response;
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

        log.debug("reviewId : {}",reviewId);
        try {
            List<CommentDto> commentList =  commentService.getComments(
                    CommentLikeDto.builder()
                            .reviewId(reviewId)
                            .userId(userId)
                            .build()
            );
            log.debug("commentList : {}", commentList);
            response = ResponseEntity.ofNullable(commentList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @GetMapping("/my_comments")
    public ResponseEntity<?> getMyComments(HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");

        ResponseEntity<?> response;
        try {
            List<CommentDto> myCommentList =  commentService.getMyComments(user.getUserId());
            log.debug("myCommentList : {}", myCommentList);
            response = ResponseEntity.ofNullable(myCommentList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    @PostMapping(value={""})
    public ResponseEntity<?> registerComment(@RequestBody CommentDto comment, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("comment : {}", comment);

        ResponseEntity<?> response;
        try {
            commentService.registerComment(
                    CommentDto.builder()
                            .userId(user.getUserId())
                            .reviewId(comment.getReviewId())
                            .content(comment.getContent())
                            .build()
            );
            response = ResponseEntity.ok().build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    //TODO: 수정 가능한 유저인지 검증
    @PutMapping("/")
    public ResponseEntity<?> modifyComment(@RequestBody CommentDto comment, HttpServletRequest request){

        UserDto user = (UserDto) request.getAttribute("user");

        ResponseEntity<?> response;
        try {
            commentService.modifyComment(
                    CommentDto.builder()
                            .commentId(comment.getCommentId())
                            .content(comment.getContent())
                            .build()
            );
            response = ResponseEntity.ok().build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    //TODO: 삭제 가능한 유저인지 검증
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable("comment_id") int commentId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");

        ResponseEntity<?> response;
        try {
            commentService.deleteComment(commentId);

            response = ResponseEntity.ok().build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    @PutMapping("/like/{comment_id}")
    public ResponseEntity<?> updateLikeCount(@PathVariable("comment_id") int commentId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("likeCount: {}", commentId);

        ResponseEntity<?> response;

        try {
            commentService.updateLikeCount(commentId);
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by deleteReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PostMapping("/like/{comment_id}")
    public ResponseEntity<?> addLike(@PathVariable("comment_id") int commentId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("addCommentLike: {}", commentId);

        ResponseEntity<?> response;

        try {
            commentService.addLike(CommentLikeDto.builder()
                    .commentId(commentId)
                    .userId(user.getUserId())
                    .build());
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by deleteReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @DeleteMapping("/like/{comment_id}")
    public ResponseEntity<?> deleteLike(@PathVariable("comment_id") int commentId, HttpServletRequest request){
        UserDto user = (UserDto) request.getAttribute("user");
        log.debug("deleteCommentLike: {}", commentId);

        ResponseEntity<?> response;

        try {
            commentService.deleteLike(CommentLikeDto.builder()
                    .commentId(commentId)
                    .userId(user.getUserId())
                    .build());
            response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Exception caused by deleteReview", e);
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }
}
