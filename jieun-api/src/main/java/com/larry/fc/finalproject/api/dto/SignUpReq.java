package com.larry.fc.finalproject.api.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data //lombok @Data 어노테이션 쓰면 필드 값에 final 붙일 수 있다 , 안정적
public class SignUpReq {
    @NotBlank
    private final String name;

    @Email
    @NotBlank
    private final String email;

    @Size(min = 6, message = "6자리 이상 입력해 주세요.")
    @NotBlank
    private final String password;

    @NotNull
    private final LocalDate birthday;
}
