package com.ssafy.gemini.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeminiDto {
    @JsonProperty("prompt_id")
    private int promptId;
    private String item;
    private String type;
    private String default_value;
}
