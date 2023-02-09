package com.larry.fc.finalproject.core.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component  //컴퍼넌트로 선언해주면, UserService에서 빈으로 주입된다.
public class BCryptEncryptor implements Encryptor{
    @Override
    public String encrypt(String origin) {
        return BCrypt.hashpw(origin, BCrypt.gensalt()); //랜덤하게 salt 생성
    }

    @Override
    public boolean isMatch(String origin, String hashed) {
        try{
            return BCrypt.checkpw(origin, hashed);
        }catch (Exception e){
            return false;
        }
    }
}
