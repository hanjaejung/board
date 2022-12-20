package com.example.sns.controller;

import com.example.sns.controller.request.UserJoinRequest;
import com.example.sns.controller.request.UserLoginRequest;
import com.example.sns.controller.response.PolymorphismResponse;
import com.example.sns.controller.response.UserJoinResponse;
import com.example.sns.controller.response.UserLoginResponse;
import com.example.sns.model.UserDto;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sns/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //UserJoinRequest를 주입한 이유도 좀더 고성능 모듈을 주입하여 입력값에서 수정사항이
    //생길경우 이 클래스에서 수정하는 일이 없어 결합성이 좀 더 낮아진다
    @PostMapping("/join")
    public PolymorphismResponse<UserJoinResponse> join(@RequestBody UserJoinRequest request){
        UserDto dto = userService.join(request.getUserName(), request.getUserPassword());
        UserJoinResponse response = UserJoinResponse.userDtoToRes(dto);

        //success메소드는 public 메소드라 UserJoinResponse클래스의 private필드값을 받으려면
        //public getter메소드가 필요하다
        //UserJoinResponse클래스에 getter메소드가 없어서 no properties discovered관련에러가 떳었다
        return PolymorphismResponse.success(response);
    }

    @PostMapping("/login")
    public PolymorphismResponse<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        String token = userService.login(request.getUserName(), request.getUserPassword());

        return PolymorphismResponse.success(new UserLoginResponse(token));
    }
}
