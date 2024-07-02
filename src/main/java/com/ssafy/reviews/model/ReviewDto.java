package com.ssafy.reviews.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.comments.model.CommentDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    @JsonProperty("review_id")
    private int reviewId;
    @JsonProperty("plan_id")
    private int planId;
    @JsonProperty("user_id")
    private int userId;
    private String title;
    private String content;
    @JsonProperty("view_count")
    private int viewCount;
    @JsonProperty("scrap_count")
    private int scrapCount;
    @JsonProperty("like_count")
    private int likeCount;
    @JsonProperty("comment_count")
    private int commentCount;
    @JsonProperty("reg_date")
    private String regDate;
    @JsonProperty("mod_date")
    private String modDate;

    private List<CommentDto> comments;
    private List<ReviewImageDto> images;

    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("is_like")
    private boolean isLike;
    @JsonProperty("is_scrap")
    private boolean isScrap;
}
