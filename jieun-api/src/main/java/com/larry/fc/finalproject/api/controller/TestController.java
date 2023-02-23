package com.larry.fc.finalproject.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TestController {
    private final JavaMailSender emailSender;

    @GetMapping("/api/mail")
    public @ResponseBody void sendTestMail(){
        final MimeMessagePreparator preparator = (MimeMessage message) -> {
            final MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo("choijiuen@gmail.com");
            helper.setSubject("제목입니다!!");
            helper.setText("여기는 테스트 내용입니다.^^");
        };
        emailSender.send(preparator);
    }

    @GetMapping("test/template")
    public String testTemplate(Model model){
        final Map<String, Object> props = new HashMap<>();
        props.put("title", "제목입니다.");
        props.put("calendar", "jiueniiii@gmail.com");
        props.put("period", "언제부터 언제까지");
        props.put("attendees", List.of("test@mail.io","test2@mail.io","test3@mail.io"));
        props.put("acceptUrl", "http://localHost:8080");    //수락하면 email에서 우리가 만든 화면으로 넘겨야 한다.
        props.put("rejectUrl", "http://localHost:8080");    //이메일에서는 api 다이렉트로 쏠 수 없으니깐
        model.addAllAttributes(props);
        return "engagement-email";  //이렇게 넘겨주면 engagement-email.html 파일 열고 템플릿에 필요한 데이터들을 넣어준다.
    }
}
