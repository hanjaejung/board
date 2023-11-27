package com.example.sns.repository;

import com.example.sns.model.entity.CommentEntity;
import com.example.sns.model.entity.ReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyEntityRepository extends JpaRepository<ReplyEntity, Long> {

    Page<ReplyEntity> findAllByComment(CommentEntity comment, Pageable pageable);
}
