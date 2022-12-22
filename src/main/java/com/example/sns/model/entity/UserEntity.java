package com.example.sns.model.entity;

import com.example.sns.model.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is NULL") //select절 수행시 삭제된거는 빼고 보여야 하기 때문에 작성
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void createdAt(){
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }

    //dto객체를 dao객체로 변환시켜주기 위한 메소드드
   public static UserEntity of(String userName, String password){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        return userEntity;
    }
}
