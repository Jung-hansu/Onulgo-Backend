package com.ssafy.users.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.plans.model.PlanDto;
import lombok.*;
import java.util.List;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @JsonProperty("user_id")
    private int userId;
    private String email;
    private String password;
    private String name;
    private String address;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("is_manager")
    private boolean isManager;
    @JsonProperty("reg_date")
    private String regDate;
    @JsonProperty("last_login")
    private String lastLogin;
    private String salt;

    private List<PlanDto> joinPlans;

}
