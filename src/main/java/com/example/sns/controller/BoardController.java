package com.example.sns.controller;

import com.example.sns.controller.request.BoardUpdateRequest;
import com.example.sns.controller.request.BoardWriteRequest;
import com.example.sns.controller.response.BoardResponse;
import com.example.sns.controller.response.PolymorphismResponse;
import com.example.sns.model.BoardDto;
import com.example.sns.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public PolymorphismResponse<Void> write(@RequestBody BoardWriteRequest request, Authentication authentication){
        boardService.write(request.getTitle(), request.getBody(), authentication.getName());
        return PolymorphismResponse.success();
    }

    @PutMapping("/{boardId}")
    public PolymorphismResponse<BoardResponse> update(@PathVariable Long boardId, @RequestBody BoardUpdateRequest request, Authentication authentication){
        BoardDto boardDto = boardService.update(request.getTitle(), request.getBody(), authentication.getName(), boardId);
        return PolymorphismResponse.success(BoardResponse.boardDtoToBoardResponse(boardDto));
    }

    @DeleteMapping("/{boardId}")
    public PolymorphismResponse<Void> delete(@PathVariable Long boardId, Authentication authentication){
        boardService.delete(authentication.getName(), boardId);
        return PolymorphismResponse.success();
    }

    @GetMapping
    public PolymorphismResponse<Page<BoardResponse>> boardList(Pageable pageable, Authentication authentication){
        return PolymorphismResponse.success(boardService.boardList(pageable).map(BoardResponse::boardDtoToBoardResponse));
    }

    @GetMapping("/my")
    public PolymorphismResponse<Page<BoardResponse>> myBoardList(Pageable pageable, Authentication authentication){
        return PolymorphismResponse.success(boardService.myBoardList(authentication.getName(), pageable).map(BoardResponse::boardDtoToBoardResponse));
    }
}
