package com.ssafy.comments.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @JsonProperty("comment_id")
    private int commentId;
    @JsonProperty("review_id")
    private int reviewId;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("user_name")
    private String userName;
    private String content;
    @JsonProperty("like_count")
    private int likeCount;
    @JsonProperty("reg_date")
    private String regDate;
    @JsonProperty("mod_date")
    private String modDate;

    @JsonProperty("is_like")
    private boolean isLike;

}
