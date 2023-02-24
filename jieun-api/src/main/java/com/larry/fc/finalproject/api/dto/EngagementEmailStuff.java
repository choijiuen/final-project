package com.larry.fc.finalproject.api.dto;


import com.larry.fc.finalproject.core.util.Period;
import lombok.Builder;
import org.springframework.data.util.Pair;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;


public class EngagementEmailStuff {
    private static final String engagementUpdateApi = "http://localhost:8080/events/engagements/";
    public static final String MAIL_TIME_FORMAT = "yyyy년 MM월 dd일(E) a hh시 mm분";
    public static final List<Pair<String, Predicate<Period>>> endAtFormatParts = Arrays.asList(
            Pair.of("yyyy년 ", period -> period.getEndAt().getYear() == period.getStartAt().getYear()),
            Pair.of("MM월 ", period -> period.getEndAt().getMonth() == period.getStartAt().getMonth()),
            Pair.of("dd일(E) ", period -> period.getEndAt().getDayOfMonth() == period.getStartAt().getDayOfMonth())
    );

    //스택을 재사용 하는지?
    //스택을 재사용하지 않고 낭비하는지? -> stack over flow
    public static String getEndAtFormat(Period period,
                                        String format,
                                        List<Pair<String, Predicate<Period>>> remainEndAtFormatParts) {
        if (remainEndAtFormatParts.isEmpty()) { //remainEndAtFormatParts가 빌 때까지 재귀호출해서 뺑뺑이
            return format;                      // 년, 월 , 일 없어지면 끝난다. 년도 부터 다르면 바로 끝나고
        } else if (remainEndAtFormatParts.get(0).getSecond().test(period)) {
            return getEndAtFormat(
                    period,
                    format.replace(remainEndAtFormatParts.get(0).getFirst(), ""),
                    remainEndAtFormatParts.subList(1, remainEndAtFormatParts.size()));
        } else {
            return format;
        }
    }
    private final Long engagementId;
    private final String toEmail;
    private final List<String> attendeeEmails;
    private final String title;
    private final Period period;
    private final String periodStr;

    @Builder
    public EngagementEmailStuff(Long engagementId,
                                String toEmail,
                                List<String> attendeeEmails,
                                String title,
                                Period period) {
        this.engagementId = engagementId;
        this.toEmail = toEmail;
        this.attendeeEmails = attendeeEmails;
        this.title = title;
        this.period = period;
        this.periodStr = getPeriodStrRecursive();
    }

    private String getPeriodStr() {
        final String startAtFormat = "yyyy년 MM월 dd일(E) a hh시 mm분";
        String endAtFormat = "yyyy년 MM월 dd일(E) a hh시 mm분";
        if (period.getEndAt().getYear() == period.getStartAt().getYear()) {
            endAtFormat = endAtFormat.replace("yyyy년 ", "");
            if (period.getEndAt().getMonth() == period.getStartAt().getMonth()) {
                endAtFormat = endAtFormat.replace("MM월 ", "");
                if (period.getEndAt().getDayOfMonth() == period.getStartAt().getDayOfMonth()) {
                    endAtFormat = endAtFormat.replace("dd일(E) ", "");
                }
            }
        }
        return period.getStartAt().format(DateTimeFormatter.ofPattern(startAtFormat)) + " - "
                + period.getEndAt().format(DateTimeFormatter.ofPattern(endAtFormat));
    }
    /** 위의 코드 고쳐서 위에 getEndAtFormat 이거 된 것
     */

    private String getPeriodStrRecursive() {
        final String endAtFormat = getEndAtFormat(period, MAIL_TIME_FORMAT, endAtFormatParts);
        return period.getStartAt().format(DateTimeFormatter.ofPattern(MAIL_TIME_FORMAT)) + " - "
                + period.getEndAt().format(DateTimeFormatter.ofPattern(endAtFormat));
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getSubject() {
        return new StringBuilder()
                .append("[초대장] ")
                .append(title)
                .append(" - ")
                .append(periodStr)
                .append("(")
                .append(toEmail)
                .append(")")
                .toString();
    }

    public Map<String, Object> getProps() {
        final Map<String, Object> props = new HashMap<>();
        props.put("title", title);
        props.put("calendar", toEmail);
        props.put("period", periodStr);
        props.put("attendees", attendeeEmails);
        props.put("acceptUrl", engagementUpdateApi + engagementId + "?type=ACCEPT");
        props.put("rejectUrl", engagementUpdateApi + engagementId + "?type=REJECT");
        return props;
    }

}