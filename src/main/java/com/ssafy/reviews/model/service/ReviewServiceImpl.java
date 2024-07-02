package com.ssafy.reviews.model.service;

import com.ssafy.comments.model.mapper.CommentMapper;
import com.ssafy.reviews.model.ReviewDto;
import com.ssafy.reviews.model.ReviewImageDto;
import com.ssafy.reviews.model.ReviewLikeDto;
import com.ssafy.reviews.model.ReviewScrapDto;
import com.ssafy.reviews.model.mapper.ReviewMapper;
import com.ssafy.util.S3FileUploadUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewMapper reviewMapper;

    private final S3FileUploadUtil s3FileUploadUtil;

    @Override
    public List<ReviewDto> getAllReviews(int userId) {
        return reviewMapper.getAllReviews(userId);
    }

    @Override
    public ReviewDto getReview(ReviewLikeDto reviewLikeDto) {
        return reviewMapper.getReview(reviewLikeDto);
    }

    @Override
    public List<ReviewDto> getReviewsOfPlan(int planId) {
        return reviewMapper.getReviewsOfPlan(planId);
    }

    @Override
    @Transactional
    public void registerReview(ReviewDto reviewDto) { reviewMapper.registerReview(reviewDto); }

    @Override
    @Transactional
    public void registerReviewImages(int reviewId, List<MultipartFile> reviewImages) throws IOException {
        List<ReviewImageDto> reviewImageList = new ArrayList<>();

        for (MultipartFile image : reviewImages) {
            String url = s3FileUploadUtil.uploadFile(image);
            reviewImageList.add(
                    ReviewImageDto.builder()
                            .reviewId(reviewId)
                            .url(url)
                            .name(image.getOriginalFilename())
                            .build()
            );
        }
        reviewMapper.registerReviewImages(reviewImageList);
    }

    @Override
    public void deleteReviewImages(int reviewId, List<ReviewImageDto> images) throws IOException {
        for (ReviewImageDto image : images) {
            s3FileUploadUtil.deleteFile(image.getUrl());
        }
        reviewMapper.deleteReviewImages(images);
    }

    @Override
    @Transactional
    public void modifyReview(ReviewDto reviewDto) {
        reviewMapper.modifyReview(reviewDto);
    }

    @Override
    @Transactional
    public void deleteReview(ReviewDto reviewDto) {
        reviewMapper.deleteReview(reviewDto);
    }

    @Override
    public void updateCommentCount(int reviewId) {
        reviewMapper.updateCommentCount(reviewId);
    }

    @Override
    public void updateLikeCount(int reviewId) {
        reviewMapper.updateLikeCount(reviewId);
    }

    @Override
    public void updateScrapCount(int reviewId) {
        reviewMapper.updateScrapCount(reviewId);
    }

    @Override
    public void addLike(ReviewLikeDto reviewLikeDto) {
        reviewMapper.addLike(reviewLikeDto);
    }

    @Override
    public void deleteLike(ReviewLikeDto reviewLikeDto) {
        reviewMapper.deleteLike(reviewLikeDto);
    }

    @Override
    public void addScrap(ReviewScrapDto reviewScrapDto) {
        reviewMapper.addScrap(reviewScrapDto);
    }

    @Override
    public void deleteScrap(ReviewScrapDto reviewScrapDto) {
        reviewMapper.deleteScrap(reviewScrapDto);
    }

    @Override
    public List<ReviewDto> getPopularReviews() {
        return reviewMapper.getPopularReviews();
    }

    @Override
    public List<ReviewImageDto> getReviewImages(int reviewId) {
        return reviewMapper.getReviewImages(reviewId);
    }

    @Override
    public List<ReviewDto> getScrapReviews(int userId) {
        return reviewMapper.getScrapReviews(userId);
    }

    @Override
    public void updateViewCount(int reviewId) {
        reviewMapper.updateViewCount(reviewId);
    }
}
