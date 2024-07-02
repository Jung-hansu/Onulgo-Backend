package com.ssafy.reviews.model.service;

import com.ssafy.reviews.model.ReviewDto;
import com.ssafy.reviews.model.ReviewImageDto;
import com.ssafy.reviews.model.ReviewLikeDto;
import com.ssafy.reviews.model.ReviewScrapDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReviewService {

    List<ReviewDto> getAllReviews(int userId);
    ReviewDto getReview(ReviewLikeDto reviewLikeDto);
    List<ReviewDto> getReviewsOfPlan(int planId);
    void registerReview(ReviewDto reviewDto);
    void registerReviewImages(int reviewId, List<MultipartFile> reviewImages) throws IOException;
    void deleteReviewImages(int reviewId, List<ReviewImageDto> fileNames) throws IOException;
    void modifyReview(ReviewDto reviewDto);
    void deleteReview(ReviewDto reviewDto);
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
