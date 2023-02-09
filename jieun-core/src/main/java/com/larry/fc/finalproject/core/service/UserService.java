package com.larry.fc.finalproject.core.service;

import com.larry.fc.finalproject.core.domain.entity.User;
import com.larry.fc.finalproject.core.domain.entity.repository.UserRepository;
import com.larry.fc.finalproject.core.dto.UserCreateReq;
import com.larry.fc.finalproject.core.util.Encryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Encryptor encryptor;
    private final UserRepository userRepository;


    @Transactional
    public User create(UserCreateReq userCreateReq) {
        userRepository.findByEmail(userCreateReq.getEmail())
                .ifPresent(u-> {
                    throw new RuntimeException("user already existed!");
                });

        return userRepository.save(new User( //유저 없는거 확인하고 새로 생성해서 저장
                userCreateReq.getName(),
                userCreateReq.getEmail(),
                encryptor.encrypt(userCreateReq.getPassword()),
                userCreateReq.getBirthday()
        ));
    }

    @Transactional
    public Optional<User> findPwMatchUser(String email, String password) {
        /**
         * 비밀번호 맞는지, 유저가 없거나, 패스워드가 맞지 않으면 optional empty 응답
         */
        return userRepository.findByEmail(email)
                .map(user -> user.isMatch(encryptor, password) ? user : null); //비밀번호가 같으면 유저 넘기고 아니면 널 넘기기
    }
}
