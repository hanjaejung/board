package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
import com.example.sns.model.BoardDto;
import com.example.sns.model.entity.BoardEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.BoardEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardEntityRepository boardEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public void write(String title, String body, String userName){

        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new SnsException(ErrorCode.USER_EXIST_NOT, String.format("%s do not exist", userName)));

        boardEntityRepository.save(BoardEntity.of(title, body, userEntity));
        //save() 메소드는 바로 DB 에 저장되지 않고 영속성 컨텍스트에 저장되었다가 flush()
        //또는 commit() 수행 시 DB에 저장됨

    }

    @Transactional
    public BoardDto update(String title, String body, String userName, Long boardId){

        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new SnsException(ErrorCode.USER_EXIST_NOT, String.format("%s do not exist", userName)));
        //board exist
        BoardEntity boardEntity = boardEntityRepository.findById(boardId).orElseThrow(() ->
                new SnsException(ErrorCode.BOARD_NOT_FOUND, String.format("%s not founded", "boardId")));


        //board permission
        if(boardEntity.getUser() != userEntity){
            throw new SnsException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, boardId));
        }

        boardEntity.setTitle(title);
        boardEntity.setBody(body);

        return BoardDto.entityToDto(boardEntityRepository.saveAndFlush(boardEntity));
        //save() 메소드와는 다르게 saveAndFlush() 메소드는 실행중(트랜잭션)에 즉시 data를 flush 한다
        //saveAndFlush() 메소드는 Spring Data JPA 에서 정의한 JpaRepository 인터페이스의 메소드이다
    }
}
