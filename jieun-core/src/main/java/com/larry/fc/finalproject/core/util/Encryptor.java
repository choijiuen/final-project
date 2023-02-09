package com.larry.fc.finalproject.core.util;

public interface Encryptor {
    String encrypt(String origin);
    boolean isMatch(String origin, String hashed);  //해쉬랑 원래랑 비교
}
