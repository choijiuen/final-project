package com.larry.fc.finalproject.api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data //lombok @Data 어노테이션 쓰면 필드 값에 final 붙일 수 있다 , 안정적
public class SignUpReq {
    private final String name;
    private final String email;
    private final String password;
    private final LocalDate birthday;
}
