package com.larry.fc.finalproject.api.service;

import com.larry.fc.finalproject.core.domain.entity.Engagement;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("dev")
@Service
public class FakeEmailService implements EmailService{

    @Override
    public void sendEngagement(Engagement engagement) {
        System.out.println("send email. email:"+ engagement.getAttendee().getEmail()
        + ", scheduleId: " + engagement.getSchedule().getId());
    }
}
