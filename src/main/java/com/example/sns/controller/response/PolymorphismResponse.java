package com.example.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PolymorphismResponse<T> {

    private String resultCode;
    private T result;

    public static PolymorphismResponse<Void> error(String errorCode){
        return new PolymorphismResponse<>(errorCode, null);
    }

    public static <T> PolymorphismResponse<T> success(T result) {
        return new PolymorphismResponse<>("SUCCESS", result);
    }

    public static <T> PolymorphismResponse<Void> success() {
        return new PolymorphismResponse<Void>("SUCCESS", null);
    }

    public String toStream(){

        if (result == null){
            return "{" +
                    "\"resultCode\":" + "\"" + resultCode + "\"," +
                    "\"result\":" + null + "}";
        }

        return "{" +
                "\"resultCode\":" + "\"" + resultCode + "\"," +
                "\"result\":" + null + "}";
    }

}
