package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.BoardException;
import com.example.sns.model.UserDto;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserDtoCacheRepository;
import com.example.sns.repository.UserEntityRepository;
import com.example.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {


    private final UserEntityRepository userEntityRepository;

    private final BCryptPasswordEncoder encoder;

    private final UserDtoCacheRepository userDtoCacheRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public UserDto loadUserByUserName(String userName){ //결국 캐시를 만든다는건 메소드로 하나 빼논다는것
        return  userDtoCacheRepository.getUser(userName).orElseGet(() -> //orElseGet은 캐시에 올라가지 않은상태일경우에 db로 체크한다는 뜻
                userEntityRepository.findByUserName(userName).map(UserDto::entityToDto).orElseThrow(
                        () -> new BoardException(ErrorCode.SAME_USER_NAME, String.format("userName is %s", userName)))
        );
    }

    @Transactional
    public UserDto join(String userName, String password){

        //ifPresent는 void타입, isPresent는 boolean
        //ifPresent는 Optional<T> 값에서 제네릭 T값이 없으면 넘어감
        //있으면 무언가를 발생기키게함
        //isPresent는 값이 있으면 true, 없으면 false 출력
        userEntityRepository.findByUserName(userName).ifPresent(it ->
        {throw new BoardException(ErrorCode.SAME_USER_NAME, String.format("%s is exist", userName));});

        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));

        return UserDto.entityToDto(userEntity);
    }

    public String login(String userName, String password){

        // 회원가입이 되어 있지 않는 경우
        //UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsException(ErrorCode.USER_EXIST_NOT, String.format("%s do not exist", userName)));
        UserDto userDto = loadUserByUserName(userName);

        userDtoCacheRepository.setUser(userDto); //로그인시 캐시셋

        //비밀번호 확인
        if(!encoder.matches(password, userDto.getPassword())){
            throw new BoardException(ErrorCode.INVALID_PASSWORD, "password wrong");
        }



        //토큰 생성
        return JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);
    }
}
