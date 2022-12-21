package com.example.sns.fixture;

import com.example.sns.model.entity.BoardEntity;
import com.example.sns.model.entity.UserEntity;

public class BoardEntityFixture {

    public static BoardEntity get(String userName, Long boardId, Long userId){
        UserEntity entity = new UserEntity();
        entity.setId(userId);
        entity.setUserName(userName);

        BoardEntity result = new BoardEntity();
        result.setUser(entity);
        result.setId(boardId);

        return result;

    }
}
