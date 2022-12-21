package com.example.sns.controller;

import com.example.sns.controller.request.BoardUpdateRequest;
import com.example.sns.controller.request.BoardWriteRequest;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
import com.example.sns.fixture.BoardEntityFixture;
import com.example.sns.model.BoardDto;
import com.example.sns.service.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BoardService boardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void boardWriteTest() throws Exception {
        mockMvc.perform(post("/api/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new BoardWriteRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void boardWriteDoNotLoginTest() throws Exception {
        mockMvc.perform(post("/api/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new BoardWriteRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value()));
    }

    @Test
    @WithMockUser
    void boardUpdateTest() throws Exception {

        when(boardService.update(eq("title"), eq("body"), any(), any())).
                thenReturn(BoardDto.entityToDto(BoardEntityFixture.get("userName", 1L, 1L)));

        mockMvc.perform(put("/api/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new BoardUpdateRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void boardUpdateDoNotLoginTest() throws Exception {
        mockMvc.perform(put("/api/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new BoardUpdateRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value()));
    }

    @Test
    @WithMockUser
    void boardUpdateDoNotMeTest() throws Exception {

        doThrow(new SnsException(ErrorCode.INVALID_PERMISSION)).when(boardService).update(eq("title"),eq("body"),any(),eq(1L));

        mockMvc.perform(put("/api/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new BoardUpdateRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void boardUpdateDoNotExistBoardTest() throws Exception {

        doThrow(new SnsException(ErrorCode.BOARD_NOT_FOUND)).when(boardService).update(eq("title"),eq("body"),any(),eq(1L));

        mockMvc.perform(put("/api/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new BoardUpdateRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
