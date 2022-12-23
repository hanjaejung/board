package com.example.sns.repository;


import com.example.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sns.model.entity.BoardEntity;

@Repository
public interface BoardEntityRepository extends JpaRepository<BoardEntity, Long> {

    Page<BoardEntity> findAllByUser(UserEntity entity, Pageable pageable);
}
