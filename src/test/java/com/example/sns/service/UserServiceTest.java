package com.example.sns.service;


import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
import com.example.sns.fixture.UserEntityFixture;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    //말그대로 MockBean은 테스트를 위햐 가상의 객체를 만들어주는것
    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void signUpTest(){

        String userName = "userName";
        String password = "password";
        //아하 when thenReturn은 무조건 나오는 값을 트루로 하는게 아니라
        //실제 메소드의 형태는 일치해야만 한다
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        //when(userEntityRepository.save(any())).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(userName, password, 1L));
        //회원가입테스트도 마찬가지로 mocking이 아닌 fixture 적용

        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @Test
    void signUpUserNameExistTest(){

        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1L);

        //이쪽도 fixture로 변경
        //when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        //Assertions.assertThrows(SnsException.class, () -> userService.join(userName, password));
        SnsException exception = Assertions.assertThrows(SnsException.class, () -> userService.join(userName, password));
        Assertions.assertEquals(ErrorCode.SAME_USER_NAME, exception.getErrorCode());
    }

    @Test
    void loginTest(){

        String userName = "userName";
        String password = "password";
        //mocking으로 가상의 구현체를 만들기보다는 fixture로 가상의 구현체를 만든다
        UserEntity fixture = UserEntityFixture.get(userName, password, 1L);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> userService.login(userName, password));
    }

    @Test
    void loginDoNotExistUserNameTest(){

        String userName = "userName";
        String password = "password";
        //실제 메소드를 돌리며 강제로 값을 정한다
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        //Assertions은 무조건 when 실행 다음엔
        // 그 안의 메소드를 실행할때 when의 결과값으로 정한다
        //그 결과값으로 비교를 진행하거나 작업을 진행한다
        //Assertions.assertThrows(SnsException.class, () -> userService.login(userName, password));
        SnsException exception = Assertions.assertThrows(SnsException.class, () -> userService.login(userName, password));

        Assertions.assertEquals(ErrorCode.USER_EXIST_NOT, exception.getErrorCode());
    }

    @Test
    void loginDoNotExistPasswordTest(){

        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1L);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        //Assertions.assertThrows(SnsException.class, () -> userService.join(userName, wrongPassword));
        SnsException exception = Assertions.assertThrows(SnsException.class, () -> userService.login(userName, wrongPassword));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }
}
