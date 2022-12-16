package com.example.sns.model;

import com.example.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String userName;
    private String password;
    private UserRole userRole;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static UserDto entityToDto(UserEntity userEntity){
        return new UserDto(
                userEntity.getId(),
                userEntity.getUserName(),
                userEntity.getPassword(),
                userEntity.getRole(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt(),
                userEntity.getDeletedAt()
        );
    }

}
