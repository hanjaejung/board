package com.example.sns.controller;

import com.example.sns.controller.request.BoardCommentRequest;
import com.example.sns.controller.request.BoardUpdateRequest;
import com.example.sns.controller.request.BoardWriteRequest;
import com.example.sns.controller.request.ReplyCommentRequest;
import com.example.sns.controller.response.BoardResponse;
import com.example.sns.controller.response.CommentResponse;
import com.example.sns.controller.response.PolymorphismResponse;
import com.example.sns.controller.response.ReplyCommentsResponse;
import com.example.sns.model.BoardDto;
import com.example.sns.model.ReplyDto;
import com.example.sns.model.entity.ReplyEntity;
import com.example.sns.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

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

    @GetMapping("/{boardId}/likes")
    public PolymorphismResponse<Integer> getLikes(@PathVariable Long boardId, Authentication authentication) {
        return PolymorphismResponse.success(boardService.getLikeCount(boardId));
    }

    @PostMapping("/{boardId}/likes")
    public PolymorphismResponse<Void> like(@PathVariable Long boardId, Authentication authentication) {
        boardService.like(boardId, authentication.getName());
        return PolymorphismResponse.success();
    }

    @GetMapping("/{boardId}/comments")
    public PolymorphismResponse<Page<CommentResponse>> getComments(Pageable pageable, @PathVariable Long boardId) {
        return PolymorphismResponse.success(boardService.getComments(boardId, pageable).map(CommentResponse::commentDtoToCommentResponse));
    }

    @PostMapping("/{boardId}/comments")
    public PolymorphismResponse<Void> comment(@PathVariable Long boardId, @RequestBody BoardCommentRequest request, Authentication authentication) {
        boardService.comment(boardId, authentication.getName(), request.getComment());
        return PolymorphismResponse.success();
    }

    @PostMapping("/{commentId}/reply")
    public PolymorphismResponse<Void> reply(@PathVariable Long commentId, @RequestBody ReplyCommentRequest request, Authentication authentication) {
        boardService.reply(commentId, authentication.getName(), request.getReply());
        return PolymorphismResponse.success();
    }

    @GetMapping("/{commentId}/replyComments")
    public PolymorphismResponse<Page<ReplyCommentsResponse>> getReplyComments(Pageable pageable, @PathVariable Long commentId) {

        return PolymorphismResponse.success(boardService.getReplyComments(commentId, pageable).map(ReplyCommentsResponse::replyDtoToReplyResponse));
    }
}
