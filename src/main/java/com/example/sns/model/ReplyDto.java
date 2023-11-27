package com.example.sns.model;

import com.example.sns.model.entity.CommentEntity;
import com.example.sns.model.entity.ReplyEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class ReplyDto {

    private Long id;
    private String replyComment;
    private Long userId;
    private String userName;
    private Long commentId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public ReplyDto() {

    }

    public static ReplyDto entityToDto(ReplyEntity entity) {
        return new ReplyDto(
                entity.getId(),
                entity.getReplyComment(),
                entity.getUser().getId(),
                entity.getUser().getUserName(),
                entity.getComment().getId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

}
