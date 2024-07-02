package com.ssafy.attractions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttractionDto {

    @JsonProperty("attraction_id")
    private int attractionId;
    @JsonProperty("view_count")
    private int viewCount;
    @JsonProperty("scrap_count")
    private int scrapCount;
    private double latitude;
    private double longitude;

}
