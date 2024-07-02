package com.ssafy.users.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("refresh_token")
    private String refreshToken;

}
