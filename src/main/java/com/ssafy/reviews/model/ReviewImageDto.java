package com.ssafy.reviews.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImageDto {

    @JsonProperty("review_image_id")
    private int reviewImageId;
    @JsonProperty("review_id")
    private int reviewId;
    @JsonProperty("url")
    private String url;
    @JsonProperty("name")
    private String name;

}
