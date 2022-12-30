package com.example.sns.repository;

import com.example.sns.model.entity.BoardEntity;
import com.example.sns.model.entity.LikeEntity;
import com.example.sns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByUserAndBoard(UserEntity user, BoardEntity board);


    //@Query(value = "SELECT COUNT(*) from LikeEntity entity WHERE entity.board = :board")
    //Integer countByPost(@Param("board") BoardEntity board);

    List<LikeEntity> findAllByBoard(BoardEntity post);


    //@Transactional
    //@Modifying
    //@Query("UPDATE LikeEntity entity SET deleted_at = NOW() where entity.post = :board")
   //void deleteAllByPost(@Param("board") BoardEntity board);
}
