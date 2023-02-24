package com.larry.fc.finalproject.api.dto;

import com.larry.fc.finalproject.core.util.Period;
import lombok.Builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EngagementEmailStuff {
    private static final String engagementUpdateApi = "http://localhost:8080/events/engagements/";
    private final Long engagementId;
    private final String toEmail;
    private final List<String> attendeEmails;
    private final String title;
    private final Period period;

    @Builder
    public EngagementEmailStuff(Long engagementId, String toEmail, List<String> attendeEmails, String title, Period period) {
        this.engagementId = engagementId;
        this.toEmail = toEmail;
        this.attendeEmails = attendeEmails;
        this.title = title;
        this.period = period;
    }

    public String getSubject(){
        return new StringBuilder()
                .append("[초대장] ")
                .append(title)
                .append(" - ")
                .append(period.toString())
                .append(")")
                .toString();
    }

    public String getToEmail(){
        return this.toEmail;
    }

    public Map<String, Object> getProps(){  //템플릿 엔진에 넣어줄 프로퍼티 응답
        final Map<String, Object> props = new HashMap<>();
        props.put("title", title);
        props.put("calendar", toEmail);
        props.put("period", period.toString());
        props.put("attendees", attendeEmails);
        props.put("acceptUrl", engagementUpdateApi + engagementId + "?type=ACCEPT");
        props.put("rejectUrl", engagementUpdateApi + engagementId + "?type=REJECT"); //파라미터 붙여서 페이지 호출
        return props;
    }
}
