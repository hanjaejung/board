package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
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

    }
}
