package com.ssafy.reviews.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewScrapDto {

    @JsonProperty("review_like_id")
    private int reviewScrapId;
    @JsonProperty("review_id")
    private int reviewId;
    @JsonProperty("user_id")
    private int userId;
}
