package com.example.sns.model;

import com.example.sns.model.entity.LikeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class LikeDto {

    private long id;
    private long userId;
    private String userName;
    private long postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static LikeDto entityToDto(LikeEntity entity) {
        return new LikeDto(
                entity.getId(),
                entity.getUser().getId(),
                entity.getUser().getUserName(),
                entity.getBoard().getId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
