package com.example.sns.model;

import com.example.sns.model.entity.BoardEntity;
import com.example.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class BoardDto {

    private Long id = null;

    private String title;

    private String body;

    private UserDto user;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static BoardDto entityToDto(BoardEntity boardEntity){
        return new BoardDto(
                boardEntity.getId(),
                boardEntity.getTitle(),
                boardEntity.getBody(),
                UserDto.entityToDto(boardEntity.getUser()),
                boardEntity.getCreatedAt(),
                boardEntity.getUpdatedAt(),
                boardEntity.getDeletedAt()
        );
    }
}
