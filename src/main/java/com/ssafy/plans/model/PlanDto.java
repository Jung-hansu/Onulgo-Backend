package com.ssafy.plans.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.users.model.UserDto;
import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDto {

    @JsonProperty("plan_id")
    private int planId;
    @JsonProperty("user_id")
    private int userId;
    private String title;
    private String desc;
    private String attractions;
    @JsonProperty("is_public")
    private boolean isPublic;
    @JsonProperty("view_count")
    private int viewCount;
    @JsonProperty("scrap_count")
    private int scrapCount;
    @JsonProperty("like_count")
    private int likeCount;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    @JsonProperty("reg_date")
    private String regDate;
    @JsonProperty("mod_date")
    private String modDate;

    @JsonProperty("join_emails")
    private List<String> joinEmails;
    @JsonProperty("join_user_ids")
    private List<Integer> joinUserIds;
    @JsonProperty("join_users")
    private List<UserDto> joinUsers;
    private List<Integer> reviews;
    @JsonProperty("is_like")
    private boolean isLike;
    @JsonProperty("is_scrap")
    private boolean isScrap;

}
