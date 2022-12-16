package com.example.sns.controller.response;

import com.example.sns.model.UserDto;
import com.example.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

    private Long id;
    private String userName;
    private UserRole role;

    public static UserJoinResponse userDtoToRes(UserDto dto){
        return new UserJoinResponse(
                dto.getId(),
                dto.getUserName(),
                dto.getUserRole()
        );
    }
}
