package com.example.sns.controller.response;

import com.example.sns.model.BoardDto;
import com.example.sns.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class BoardResponse {
    private Long id;

    private String title;

    private String body;

    private UserResponse user;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static BoardResponse boardDtoToBoardResponse(BoardDto dto) {
        return new BoardResponse(
                dto.getId(),
                dto.getTitle(),
                dto.getBody(),
                UserResponse.userDtoUserResponse(dto.getUser()),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getDeletedAt()
        );
    }
}
