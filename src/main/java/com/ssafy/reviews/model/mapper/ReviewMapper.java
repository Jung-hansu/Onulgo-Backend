package com.ssafy.reviews.model.mapper;

import com.ssafy.reviews.model.ReviewDto;
import com.ssafy.reviews.model.ReviewImageDto;
import com.ssafy.reviews.model.ReviewLikeDto;
import com.ssafy.reviews.model.ReviewScrapDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface ReviewMapper {

    List<ReviewDto> getAllReviews(int userId);
    ReviewDto getReview(ReviewLikeDto reviewLikeDto);
    List<ReviewDto> getReviewsOfPlan(int planId);
    void registerReview(ReviewDto reviewDto);
    void registerReviewImages(List<ReviewImageDto> reviewImages);
    void modifyReview(ReviewDto reviewDto);
    void deleteReview(ReviewDto reviewDto);
    void deleteReviewImages(List<ReviewImageDto> reviewImages);
    void updateCommentCount(int reviewId);
    void updateLikeCount(int reviewId);
    void updateScrapCount(int reviewId);
    void addLike(ReviewLikeDto reviewLikeDto);
    void deleteLike(ReviewLikeDto reviewLikeDto);
    void addScrap(ReviewScrapDto reviewScrapDto);
    void deleteScrap(ReviewScrapDto reviewScrapDto);
    List<ReviewDto> getPopularReviews();
    List<ReviewImageDto> getReviewImages(int reviewId);
    List<ReviewDto>getScrapReviews(int userId);
    void updateViewCount(int reviewId);
}
