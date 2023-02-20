package com.larry.fc.finalproject.api.service;

import com.larry.fc.finalproject.core.domain.entity.Engagement;

public interface EmailService {
    void sendEngagement(Engagement engagement);
}
