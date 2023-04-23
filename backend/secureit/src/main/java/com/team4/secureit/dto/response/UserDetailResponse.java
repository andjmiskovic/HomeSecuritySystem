package com.team4.secureit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String city;
}
