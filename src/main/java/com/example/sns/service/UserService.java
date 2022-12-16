package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
import com.example.sns.model.UserDto;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserEntityRepository userEntityRepository;

    public UserDto join(String userName, String password){

        //ifPresent는 void타입, isPresent는 boolean
        //ifPresent는 Optional<T> 값에서 제네릭 T값이 없으면 넘어감
        //있으면 무언가를 발생기키게함
        //isPresent는 값이 있으면 true, 없으면 false 출력
        userEntityRepository.findByUserName(userName).ifPresent(it ->
        {throw new SnsException(ErrorCode.SAME_USER_NAME, String.format("%s is exist", userName));});

        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, password));

        return UserDto.entityToDto(userEntity);
    }

    public String login(String userName, String password){

        // 회원가입이 되어 있는 경우
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsException(ErrorCode.SAME_USER_NAME, ""));

        //비밀번호 확인
        if(!userEntity.getPassword().equals(password)){
            throw new SnsException(ErrorCode.SAME_USER_NAME, "");
        }

        //토큰 생성


        return "";
    }
}
