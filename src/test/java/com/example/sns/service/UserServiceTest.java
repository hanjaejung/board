package com.example.sns.service;


import com.example.sns.exception.SnsException;
import com.example.sns.fixture.UserEntityFixture;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    void signUpTest(){

        String userName = "userName";
        String password = "password";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        //when(userEntityRepository.save(any())).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userEntityRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));
        //회원가입테스트도 마찬가지로 mocking이 아닌 fixture 적용

        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @Test
    void signUpUserNameExistTest(){

        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        //이쪽도 fixture로 변경
        //when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        Assertions.assertThrows(SnsException.class, () -> userService.join(userName, password));
    }

    @Test
    void loginTest(){

        String userName = "userName";
        String password = "password";
        //mocking으로 가상의 구현체를 만들기보다는 fixture로 가상의 구현체를 만든다
        UserEntity fixture = UserEntityFixture.get(userName, password);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        Assertions.assertDoesNotThrow(() -> userService.login(userName, password));
    }

    @Test
    void loginDoNotExistUserNameTest(){

        String userName = "userName";
        String password = "password";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        Assertions.assertThrows(SnsException.class, () -> userService.join(userName, password));
    }

    @Test
    void loginDoNotExistPasswordTest(){

        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        Assertions.assertThrows(SnsException.class, () -> userService.join(userName, wrongPassword));
    }
}
