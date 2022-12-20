package com.example.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardWriteRequest {
    private String title;
    private String body;
}
