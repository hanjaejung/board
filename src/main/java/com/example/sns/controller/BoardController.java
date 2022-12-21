package com.example.sns.controller;

import com.example.sns.controller.request.BoardUpdateRequest;
import com.example.sns.controller.request.BoardWriteRequest;
import com.example.sns.controller.response.BoardResponse;
import com.example.sns.controller.response.PolymorphismResponse;
import com.example.sns.model.BoardDto;
import com.example.sns.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
}
