package com.ssafy.reviews.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLikeDto {

    @JsonProperty("review_like_id")
    private int reviewLikeId;
    @JsonProperty("review_id")
    private int reviewId;
    @JsonProperty("user_id")
    private int userId;
}
