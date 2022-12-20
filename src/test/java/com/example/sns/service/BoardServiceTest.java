package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
import com.example.sns.model.entity.BoardEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.BoardEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.mock;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @MockBean
    private BoardEntityRepository boardEntityRepository;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    void boardWriteTest() throws Exception {
        String title = "title";
        String body = "body";
        String userName = "userName";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(boardEntityRepository.save(any())).thenReturn(mock(BoardEntity.class));
        Assertions.assertDoesNotThrow(() -> boardService.write(title, body, userName));

    }

    @Test
    void boardWriteDoNotLoginTest() throws Exception {
        String title = "title";
        String body = "body";
        String userName = "userName";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(boardEntityRepository.save(any())).thenReturn(mock(BoardEntity.class));
        SnsException exception = Assertions.assertThrows(SnsException.class, () -> boardService.write(title, body, userName));
        Assertions.assertEquals(ErrorCode.USER_EXIST_NOT, exception.getErrorCode());
    }
}
