package com.example.sns.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sns.model.entity.BoardEntity;

@Repository
public interface BoardEntityRepository extends JpaRepository<BoardEntity, Long> {
}
