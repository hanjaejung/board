package com.example.sns.repository;

import com.example.sns.model.entity.BoardEntity;
import com.example.sns.model.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByBoard(BoardEntity post, Pageable pageable);

    //@Transactional
    //@Modifying
    //@Query("UPDATE CommentEntity entity SET deleted_at = NOW() where entity.board = :board")
    //void deleteAllByBoard(@Param("board") BoardEntity board);
}
