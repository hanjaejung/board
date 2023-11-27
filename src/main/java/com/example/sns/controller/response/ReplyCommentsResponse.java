package com.example.sns.controller.response;

import com.example.sns.model.CommentDto;
import com.example.sns.model.ReplyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class ReplyCommentsResponse {

    private Long id;
    private String replyComment;
    private Long userId;
    private String userName;
    private Long commentId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public ReplyCommentsResponse() {

    }

    public static ReplyCommentsResponse replyDtoToReplyResponse(ReplyDto reply) {
        return new ReplyCommentsResponse(
                reply.getId(),
                reply.getReplyComment(),
                reply.getUserId(),
                reply.getUserName(),
                reply.getCommentId(),
                reply.getRegisteredAt(),
                reply.getUpdatedAt(),
                reply.getDeletedAt()
        );
    }
}
