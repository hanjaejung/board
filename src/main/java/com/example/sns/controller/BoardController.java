package com.example.sns.controller;

import com.example.sns.controller.request.BoardWriteRequest;
import com.example.sns.controller.response.PolymorphismResponse;
import com.example.sns.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
