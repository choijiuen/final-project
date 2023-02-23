package com.larry.fc.finalproject.api.service;

import com.larry.fc.finalproject.api.dto.LoginReq;
import com.larry.fc.finalproject.api.dto.SignUpReq;
import com.larry.fc.finalproject.core.domain.entity.User;
import com.larry.fc.finalproject.core.dto.UserCreateReq;
import com.larry.fc.finalproject.core.exception.CalendarException;
import com.larry.fc.finalproject.core.exception.ErrorCode;
import com.larry.fc.finalproject.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor //서비스는 하위에 여러가지 빈을 주입받게 되어서 그 타입에 맞는 생성자를 롬복이 자동 생성
public class LoginService { //로그인, 로그아웃 기능 구현

    public final static String LOGIN_SESSION_KEY = "USER_ID";
    private final UserService userService;

    @Transactional
    public void signUp(SignUpReq signUpReq, HttpSession session){ //sign up 메소드 만들기
        /**
         * UserService 에 Create 를 담당한다. (이미 존재하는 경우의 유저 검증은 userService의 몫)
         * 생성이 되면 session에 담고 리턴
         * 회원 가입용 객체를 받아서 유저에게 create 위임 , 세션에 값을 넣고 끝냄
         */
        final User user = userService.create(new UserCreateReq(
                signUpReq.getName(),
                signUpReq.getEmail(),
                signUpReq.getPassword(),
                signUpReq.getBirthday()
        )); //UserService에 Create 요청 날리기
        session.setAttribute(LOGIN_SESSION_KEY, user.getId());

    }

    @Transactional
    public void login(LoginReq loginReq, HttpSession session){
        /**
         * 세션 값이 있으면 리턴
         * 세션 값이 없으면 비밀번호 체크 후에 로그인 & 세션에 담고 리턴
         * 로그인 리퀘스트를 받아서 비밀번호 맞는 유저 찾기 , 전에 세션에 값이 있는지 먼저 체크
         */
        final Long userId = (Long)session.getAttribute(LOGIN_SESSION_KEY);
        if(userId != null){ //login 한 상태
            return;
        }
        final Optional<User> user= userService.findPwMatchUser(loginReq.getEmail(), loginReq.getPassword());
        if(user.isPresent()){
            session.setAttribute(LOGIN_SESSION_KEY, user.get().getId());
        }else{
            throw new CalendarException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    public void logout(HttpSession session){
        /**
         * 세션 제거하고 끝
         */
        session.removeAttribute(LOGIN_SESSION_KEY);
    }
}
