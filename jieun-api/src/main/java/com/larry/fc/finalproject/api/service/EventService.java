package com.larry.fc.finalproject.api.service;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.EngagementEmailStuff;
import com.larry.fc.finalproject.api.dto.EventCreateReq;
import com.larry.fc.finalproject.core.domain.RequestStatus;
import com.larry.fc.finalproject.core.domain.entity.Engagement;
import com.larry.fc.finalproject.core.domain.entity.Schedule;
import com.larry.fc.finalproject.core.domain.entity.User;
import com.larry.fc.finalproject.core.domain.entity.repository.EngagementRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.ScheduleRepository;
import com.larry.fc.finalproject.core.exception.CalendarException;
import com.larry.fc.finalproject.core.exception.ErrorCode;
import com.larry.fc.finalproject.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EmailService emailService;
    private final UserService userService;
    private final ScheduleRepository scheduleRepository;
    private final EngagementRepository engagementRepository;

    @Transactional
    public void create(EventCreateReq eventCreateReq, AuthUser authUser) {
        // 이벤트 참여자의 다른 이벤트와 중복이 되면 안된다.
        // 1-2까지 회의가 있는데, 추가로 다른 회의를 등록할 수 없음.
        // 추가로 이메일 발송
        final List<Engagement> engagementList = engagementRepository.findAll(); //TODO finaAll 개선
        if(engagementList.stream()
                .anyMatch(e -> eventCreateReq.getAttendeeIds().contains(e.getAttendee().getId())
                && e.getRequestStatus() == RequestStatus.ACCEPTED //수락 상태 engagement 있을 때
                && e.getEvent().isOverlapped(
                eventCreateReq.getStartAt(),
                eventCreateReq.getEndAt()))

        ){ //engagement 중에 아무거나 하나 걸리면 , 기간이랑 겹치는지, 약속 상태가 수락인지 등
            throw new CalendarException(ErrorCode.EVENT_CREATE_OVERLAPPED_PERIOD);
        }
        final Schedule eventSchedule = Schedule.event( //걸리는게
                eventCreateReq.getTitle(),
                eventCreateReq.getDescription(),
                eventCreateReq.getStartAt(),
                eventCreateReq.getEndAt(),
                userService.findByUserId(authUser.getId())
        );
        scheduleRepository.save(eventSchedule); //eventSchedule 생성하고 db 저장

        final List<User> attendees =
                eventCreateReq.getAttendeeIds().stream()
                        .map(aId -> userService.findByUserId(aId))
                        .collect(Collectors.toList());

        attendees.forEach(attendee -> {
                    final Engagement engagement = Engagement.builder()
                            .schedule(eventSchedule)
                            .requestStatus(RequestStatus.REQUESTED)
                            .attendee(attendee)
                            .build();
                    engagementRepository.save(engagement); //engagement 생성하고 db 저장
                    emailService.sendEngagement(EngagementEmailStuff.builder()
                                    .engagementId(engagement.getId())
                            .title(engagement.getEvent().getTitle())
                            .toEmail(engagement.getAttendee().getEmail())
                            .attendeEmails(attendees.stream()
                                    .map(a -> a.getEmail())
                                    .collect(Collectors.toList())) //engagement에서는 참석자 목록 모르니깐 위에서 어텐디 목록 뽑음
                            .period(engagement.getEvent().getPeriod())
                            .build());
        });
    }
}
