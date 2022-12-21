package com.example.sns.controller.response;

import com.example.sns.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String userName;

    public static UserResponse userDtoUserResponse(UserDto user) {
        return new UserResponse(
                user.getId(),
                user.getUsername()
        );
    }
}
