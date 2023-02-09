package com.larry.fc.finalproject.core.domain.entity;

import com.larry.fc.finalproject.core.util.Encryptor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Table(name="users")
@Entity
public class User extends BaseEntity{

    private String name;
    private String email;
    private String password;
    private LocalDate birthday;

    public User(String name, String email, String password, LocalDate birthday) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
    }

    public boolean isMatch(Encryptor encryptor, String password) {
        return encryptor.isMatch(password, this.password); //strategy pattern
        //인터페이스를 인자로 넘겨서 기능 위임 , 유저에 대한 기능 테스트하기 매우 편리
    }
}
