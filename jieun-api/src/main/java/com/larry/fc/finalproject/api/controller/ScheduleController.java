package com.larry.fc.finalproject.api.controller;


import com.larry.fc.finalproject.api.dto.*;
import com.larry.fc.finalproject.api.service.*;
import com.larry.fc.finalproject.core.domain.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static com.larry.fc.finalproject.api.service.LoginService.LOGIN_SESSION_KEY;

@RequiredArgsConstructor
@RequestMapping("/api/schedules")
@RestController
public class ScheduleController {

    private final ScheduleQueryService scheduleQueryService;
    //세가지 기능에 관련된 도메인에 대해서 조회만을 담당하는 서비스,
    private final TaskService taskService;
    private final EventService eventService;
    private final EngagementService engagementService;
    private final NotificationService notificationService;

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(
            @RequestBody TaskCreateReq taskCreateReq,
            AuthUser authUser){
        taskService.create(taskCreateReq, authUser); //userId 있을 때 넘기기 -> AuthUser 쓰고 날렸음
        return ResponseEntity.ok().build();
    }

    @PostMapping("/events")
    public ResponseEntity<Void> createEvent(
            @Valid @RequestBody EventCreateReq eventCreateReq, //@Valid 달면 eventCreateReq 에 달아놨던 어노테이션 동작 하는 거다
            AuthUser authUser){
        eventService.create(eventCreateReq, authUser); //userId 있을 때 넘기기 -> AuthUser 쓰고 날렸음
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications")
    public ResponseEntity<Void> createNotification(
            @RequestBody NotificationCreateReq notificationCreateReq,
            AuthUser authUser){
        notificationService.create(notificationCreateReq, authUser); //userId 있을 때 넘기기 -> AuthUser 쓰고 날렸음
        return ResponseEntity.ok().build();
    }

    @GetMapping("/day")
    public List<ScheduleDto> getScheduleByDay(
            AuthUser authUser,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return scheduleQueryService.getScheduleByDay(authUser, date == null ? LocalDate.now() : date); //date 값이 null이면 현재를 보여주고 아니면 date 값 넣기
    }

    @GetMapping("/week")
    public List<ScheduleDto> getScheduleByWeek(
            AuthUser authUser,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startOfWeek){
        return scheduleQueryService.getScheduleByWeek(authUser, startOfWeek == null ? LocalDate.now() : startOfWeek); //date 값이 null이면 현재를 보여주고 아니면 date 값 넣기
    }

    @GetMapping("/month")
    public List<ScheduleDto> getScheduleByMonth(
            AuthUser authUser,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM") String yearMonth){ //2023-02
        return scheduleQueryService.getScheduleByMonth(authUser, yearMonth == null ? YearMonth.now() : YearMonth.parse(yearMonth)); //date 값이 null이면 현재를 보여주고 아니면 date 값 넣기
    }

    @PutMapping("/events/engagement/{engagmentId}")
    public RequestStatus updateEngagement(
            @Valid @RequestBody ReplyEngagementReq replyEngagementReq,
            @PathVariable Long engagementId,
            AuthUser authUser){
        return engagementService.update(authUser, engagementId, replyEngagementReq.getType());
        }
    }



