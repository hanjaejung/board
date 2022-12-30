package com.example.sns.service;

import com.example.sns.controller.request.BoardCommentRequest;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.BoardException;
import com.example.sns.fixture.BoardEntityFixture;
import com.example.sns.fixture.UserEntityFixture;
import com.example.sns.model.entity.BoardEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.BoardEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        BoardException exception = Assertions.assertThrows(BoardException.class, () -> boardService.write(title, body, userName));
        Assertions.assertEquals(ErrorCode.USER_EXIST_NOT, exception.getErrorCode());
    }

    @Test
    void boardUpdateTest() throws Exception {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Long id = 1L;

        BoardEntity entity = BoardEntityFixture.get(userName,id, 1l);
        UserEntity userEntity = entity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(boardEntityRepository.findById(id)).thenReturn(Optional.of(entity));
        when(boardEntityRepository.saveAndFlush(any())).thenReturn(entity);

        Assertions.assertDoesNotThrow(() -> boardService.update(title, body, userName, id));
    }

    @Test
    void boardUpdateDoNotExistBoardTest() throws Exception {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Long id = 1L;

        BoardEntity entity = BoardEntityFixture.get(userName,id, 1l);
        UserEntity userEntity = entity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(boardEntityRepository.findById(id)).thenReturn(Optional.empty());

        BoardException exception = Assertions.assertThrows(BoardException.class, () -> boardService.update(title, body, userName, id));
        Assertions.assertEquals(ErrorCode.BOARD_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void boardUpdateDoNotExistAuthorityTest() throws Exception {
        String title = "title";
        String body = "body";
        String userName = "userName";
        Long id = 1L;

        BoardEntity entity = BoardEntityFixture.get(userName,id, 1L);
        UserEntity writerEntity = UserEntityFixture.get("AnotherUserName", "password", 2l);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writerEntity));
        when(boardEntityRepository.findById(id)).thenReturn(Optional.of(entity));

        BoardException exception = Assertions.assertThrows(BoardException.class, () -> boardService.update(title, body, userName, id));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void boardDeleteTest() throws Exception {
        String userName = "userName";
        Long id = 1L;

        BoardEntity entity = BoardEntityFixture.get(userName,id, 1l);
        UserEntity userEntity = entity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(boardEntityRepository.findById(id)).thenReturn(Optional.of(entity));

        Assertions.assertDoesNotThrow(() -> boardService.delete(userName, 1L));
    }

    @Test
    void boardDeleteDoNotExistBoardTest() throws Exception {
        String userName = "userName";
        Long id = 1L;

        BoardEntity entity = BoardEntityFixture.get(userName,id, 1l);
        UserEntity userEntity = entity.getUser();

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(boardEntityRepository.findById(id)).thenReturn(Optional.empty());

        BoardException exception = Assertions.assertThrows(BoardException.class, () -> boardService.delete(userName, 1L));
        Assertions.assertEquals(ErrorCode.BOARD_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void boardDeleteDoNotExistAuthorityTest() throws Exception {
        String userName = "userName";
        Long id = 1L;

        BoardEntity entity = BoardEntityFixture.get(userName,id, 1L);
        UserEntity writerEntity = UserEntityFixture.get("AnotherUserName", "password", 2l);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writerEntity));
        when(boardEntityRepository.findById(id)).thenReturn(Optional.of(entity));

        BoardException exception = Assertions.assertThrows(BoardException.class, () -> boardService.delete(userName, 1L));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void boardListTest() throws Exception {

        Pageable pageable = mock(Pageable.class);
        when(boardEntityRepository.findAll(pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> boardService.boardList(pageable));
    }

    @Test
    void myBoardListTest() throws Exception {

        Pageable pageable = mock(Pageable.class);
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntityRepository.findByUserName(any())).thenReturn(Optional.of(userEntity));
        when(boardEntityRepository.findAllByUser(userEntity, pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> boardService.myBoardList("", pageable));
    }

    @Test
    void likeTest() throws Exception {
        String userName = "userName";
        Long id = 1L;

        BoardEntity entity = BoardEntityFixture.get(userName,id, 1l);
        UserEntity userEntity = entity.getUser();

        when(boardEntityRepository.findById(id)).thenReturn(Optional.of(entity));
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));

        Assertions.assertDoesNotThrow(() -> boardService.like(id, userName));
        //이 테스트 에러나는 이유 위의 findById, findByUserName에서 에러가 안나도
        //like 서비스에 있는 동일한 userName이 있다는 ALREADY_LIKED_BOARD에러에 걸린다
    }

    @Test
    void doNotLoginLikeTest() throws Exception {
            String userName = "userName";
            Long id = 1L;

            BoardEntity entity = BoardEntityFixture.get(userName,id, 1l);
            UserEntity userEntity = entity.getUser();

            when(boardEntityRepository.findById(id)).thenReturn(Optional.of(entity));
            when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

            BoardException exception = Assertions.assertThrows(BoardException.class, () -> boardService.like(id, userName));
            Assertions.assertEquals(ErrorCode.USER_EXIST_NOT, exception.getErrorCode());
    }

    @Test
    void doNotExistBoardLikeTest() throws Exception {
            String userName = "userName";
            Long id = 1L;

            BoardEntity entity = BoardEntityFixture.get(userName,id, 1l);

            when(boardEntityRepository.findById(id)).thenReturn(Optional.empty());

            BoardException exception = Assertions.assertThrows(BoardException.class, () -> boardService.like(id, userName));
            Assertions.assertEquals(ErrorCode.BOARD_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void commentWriteTest() throws Exception {
        String userName = "userName";
        Long id = 1L;

        BoardEntity entity = BoardEntityFixture.get(userName,id, 1l);
        UserEntity userEntity = entity.getUser();

        when(boardEntityRepository.findById(id)).thenReturn(Optional.of(entity));
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));

        Assertions.assertDoesNotThrow(() -> boardService.comment(id, userName, "testComment"));
    }


    @Test
    void doNotLoginCommentWriteTest() throws Exception {
        String userName = "userName";
            Long id = 1L;

            BoardEntity entity = BoardEntityFixture.get(userName,id, 1l);
            UserEntity userEntity = entity.getUser();

            when(boardEntityRepository.findById(id)).thenReturn(Optional.of(entity));
            when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

            BoardException exception = Assertions.assertThrows(BoardException.class, () -> boardService.comment(id, userName, "testComment"));
            Assertions.assertEquals(ErrorCode.USER_EXIST_NOT, exception.getErrorCode());
    }

    @Test
    void doNotExistBoardCommentWriteTest() throws Exception {
        String userName = "userName";
            Long id = 1L;

            BoardEntity entity = BoardEntityFixture.get(userName,id, 1l);

            when(boardEntityRepository.findById(id)).thenReturn(Optional.empty());

            BoardException exception = Assertions.assertThrows(BoardException.class, () -> boardService.comment(id, userName, "testComment"));
            Assertions.assertEquals(ErrorCode.BOARD_NOT_FOUND, exception.getErrorCode());
    }
}
