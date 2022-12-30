package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.BoardException;
import com.example.sns.model.BoardDto;
import com.example.sns.model.CommentDto;
import com.example.sns.model.entity.BoardEntity;
import com.example.sns.model.entity.CommentEntity;
import com.example.sns.model.entity.LikeEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.BoardEntityRepository;
import com.example.sns.repository.CommentEntityRepository;
import com.example.sns.repository.LikeEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardEntityRepository boardEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;

    @Transactional
    public void write(String title, String body, String userName){

        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new BoardException(ErrorCode.USER_EXIST_NOT, String.format("%s do not exist", userName)));

        boardEntityRepository.save(BoardEntity.of(title, body, userEntity));
        //save() 메소드는 바로 DB 에 저장되지 않고 영속성 컨텍스트에 저장되었다가 flush()
        //또는 commit() 수행 시 DB에 저장됨

    }

    @Transactional
    public BoardDto update(String title, String body, String userName, Long boardId){

        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new BoardException(ErrorCode.USER_EXIST_NOT, String.format("%s do not exist", userName)));
        //board exist
        BoardEntity boardEntity = boardEntityRepository.findById(boardId).orElseThrow(() ->
                new BoardException(ErrorCode.BOARD_NOT_FOUND, String.format("%s not founded", "boardId")));


        //board permission
        if(boardEntity.getUser() != userEntity){
            throw new BoardException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, boardId));
        }

        boardEntity.setTitle(title);
        boardEntity.setBody(body);

        return BoardDto.entityToDto(boardEntityRepository.saveAndFlush(boardEntity));
        //save() 메소드와는 다르게 saveAndFlush() 메소드는 실행중(트랜잭션)에 즉시 data를 flush 한다
        //saveAndFlush() 메소드는 Spring Data JPA 에서 정의한 JpaRepository 인터페이스의 메소드이다
    }

    @Transactional
    public void delete(String userName, Long boardId){

        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new BoardException(ErrorCode.USER_EXIST_NOT, String.format("%s do not exist", userName)));

        BoardEntity boardEntity = boardEntityRepository.findById(boardId).orElseThrow(() ->
                new BoardException(ErrorCode.BOARD_NOT_FOUND, String.format("%s not founded", "boardId")));

        if(boardEntity.getUser() != userEntity){
            throw new BoardException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, boardId));
        }

        boardEntityRepository.delete(boardEntity);
    }

    public Page<BoardDto> boardList(Pageable pageable){

        return boardEntityRepository.findAll(pageable).map(BoardDto::entityToDto);
    }

    public Page<BoardDto> myBoardList(String userName, Pageable pageable){

        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new BoardException(ErrorCode.USER_EXIST_NOT, String.format("%s do not exist", userName)));

        return boardEntityRepository.findAllByUser(userEntity, pageable).map(BoardDto::entityToDto);

    }

    @Transactional
    public void like(Long boardId, String userName) {

        BoardEntity boardEntity = boardEntityRepository.findById(boardId).orElseThrow(() ->
                new BoardException(ErrorCode.BOARD_NOT_FOUND, String.format("%s not founded", "boardId")));

        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new BoardException(ErrorCode.USER_EXIST_NOT, String.format("%s do not exist", userName)));

        likeEntityRepository.findByUserAndBoard(userEntity, boardEntity).ifPresent(it -> {
            throw new BoardException(ErrorCode.ALREADY_LIKED_BOARD, String.format("userName %s already like the board %s", userName, boardId));
        });

        likeEntityRepository.save(LikeEntity.of(boardEntity, userEntity));
    }

    public int getLikeCount(Long boardId) {
        BoardEntity boardEntity = boardEntityRepository.findById(boardId).orElseThrow(() ->
                new BoardException(ErrorCode.BOARD_NOT_FOUND, String.format("%s not founded", "boardId")));
        List<LikeEntity> likes = likeEntityRepository.findAllByBoard(boardEntity);
        return likes.size();
    }

    @Transactional
    public void comment(Long boardId, String userName, String comment) {
        BoardEntity boardEntity = boardEntityRepository.findById(boardId).orElseThrow(() ->
                new BoardException(ErrorCode.BOARD_NOT_FOUND, String.format("%s not founded", "boardId")));
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new BoardException(ErrorCode.USER_EXIST_NOT, String.format("%s do not exist", userName)));

        commentEntityRepository.save(CommentEntity.of(comment, boardEntity, userEntity));

    }

    public Page<CommentDto> getComments(Long boardId, Pageable pageable) {
        BoardEntity boardEntity = boardEntityRepository.findById(boardId).orElseThrow(() ->
                new BoardException(ErrorCode.BOARD_NOT_FOUND, String.format("%s not founded", "boardId")));
        return commentEntityRepository.findAllByBoard(boardEntity, pageable).map(CommentDto::entityToDto);
    }
}
