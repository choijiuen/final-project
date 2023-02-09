package com.larry.fc.finalproject.api.controller;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

import static com.larry.fc.finalproject.api.service.LoginService.LOGIN_SESSION_KEY;

@Controller
public class IndexController {

    @GetMapping("/") //domain 접속시 아래 메서드 실행
    public String index(Model model, HttpSession session,
                        @RequestParam(required = false) String redirect){
        model.addAttribute("isSignIn", session.getAttribute(LOGIN_SESSION_KEY) != null);
        model.addAttribute("redirect");
        return "index";
    }

}
