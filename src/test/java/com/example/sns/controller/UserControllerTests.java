package com.example.sns.controller;

import com.example.sns.controller.request.UserJoinRequest;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.BoardException;
import com.example.sns.model.UserDto;
import com.example.sns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void signUpTest() throws Exception{
        String userName = "userName";
        String passWord = "password";

        // TODO : mocking
        when(userService.join(userName, passWord)).thenReturn(mock(UserDto.class));

        mockMvc.perform(post("/api/board/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                //TODO : add request body
                //writeValueAsBytes는 Java 오브젝트로 부터 JSON을 만들고 이를 문자열 혹은 Byte 배열로 반환
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, passWord)))
        ).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void signUpUserNameFailTest() throws Exception{
        String userName = "userName";
        String passWord = "password";

        // TODO : mocking
        //다른 exception이 올 수 있으니 고효율 모듈인 exception클래스를 하나더 만들어
        //이 부분을 수정안하게 한다
        when(userService.join(userName, passWord)).thenThrow(new BoardException(ErrorCode.SAME_USER_NAME));
        System.out.println("????????? : " + ErrorCode.SAME_USER_NAME.getStatus().value());
        mockMvc.perform(post("/api/board/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        //TODO : add request body
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, passWord)))
                ).andDo(print())
                .andExpect(status().is(ErrorCode.SAME_USER_NAME.getStatus().value()));

    }

    @Test
    public void loginTest() throws Exception{
        String userName = "userName";
        String password = "password";

        // TODO : mocking
        when(userService.login(userName, password)).thenReturn("");

        mockMvc.perform(post("/api/board/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        //TODO : add request body
                        //writeValueAsBytes는 Java 오브젝트로 부터 JSON을 만들고 이를 문자열 혹은 Byte 배열로 반환
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void loginWrongUserName() throws Exception{
        String userName = "userName";
        String password = "password";

        // TODO : mocking
        when(userService.login(userName, password)).thenThrow(new BoardException(ErrorCode.USER_EXIST_NOT));

        mockMvc.perform(post("/api/board/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        //TODO : add request body
                        //writeValueAsBytes는 Java 오브젝트로 부터 JSON을 만들고 이를 문자열 혹은 Byte 배열로 반환
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void loginWrongPassword() throws Exception{
        String userName = "userName";
        String password = "password";

        // TODO : mocking
        when(userService.login(userName, password)).thenThrow(new BoardException(ErrorCode.INVALID_PASSWORD, ""));

        mockMvc.perform(post("/api/board/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        //TODO : add request body
                        //writeValueAsBytes는 Java 오브젝트로 부터 JSON을 만들고 이를 문자열 혹은 Byte 배열로 반환
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());

    }
}
