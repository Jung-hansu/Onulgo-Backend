package com.ssafy.comments.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikeDto {

    @JsonProperty("comment_like_id")
    private int commentLikeId;
    @JsonProperty("comment_id")
    private int commentId;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("review_id")
    private int reviewId;
}
