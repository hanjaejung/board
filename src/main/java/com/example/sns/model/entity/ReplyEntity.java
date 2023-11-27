package com.example.sns.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "reply")
@SQLDelete(sql = "UPDATE reply SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is NULL")
@NoArgsConstructor
public class ReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(name = "replyComment")
    private String replyComment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    public static ReplyEntity of(CommentEntity comment, String reply, UserEntity user) {
        ReplyEntity entity = new ReplyEntity();
        entity.setReplyComment(reply);
        entity.setUser(user);
        entity.setComment(comment);
        return entity;
    }
}
