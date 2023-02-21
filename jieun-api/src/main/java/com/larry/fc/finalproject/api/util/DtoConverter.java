package com.larry.fc.finalproject.api.util;

import com.larry.fc.finalproject.api.dto.EventDto;
import com.larry.fc.finalproject.api.dto.NotificationDto;
import com.larry.fc.finalproject.api.dto.ScheduleDto;
import com.larry.fc.finalproject.api.dto.TaskDto;
import com.larry.fc.finalproject.core.domain.Event;
import com.larry.fc.finalproject.core.domain.entity.Schedule;

public abstract class DtoConverter {

    public static ScheduleDto fromSchedule(Schedule schedule){
        switch (schedule.getScheduleType()){ //스케줄 타입이 어떤거냐에 따라서
            case EVENT: //스케줄 dto의 구현체들을 응답하면 된다.
                return EventDto.builder()
                        .scheduleId(schedule.getId())
                        .description(schedule.getDescription())
                        .startAt(schedule.getStartAt())
                        .endAt(schedule.getEndAt())
                        .title(schedule.getTitle())
                        .writerId(schedule.getWriter().getId())
                        .build();
            case TASK:
                return TaskDto.builder()
                        .scheduleId(schedule.getId())
                        .taskAt(schedule.getStartAt())
                        .description(schedule.getDescription())
                        .writerId(schedule.getWriter().getId())
                        .title(schedule.getTitle())
                        .build();
            case NOTIFICATION:
                return NotificationDto.builder()
                        .notifyAt(schedule.getStartAt())
                        .scheduleId(schedule.getId())
                        .title(schedule.getTitle())
                        .writerId(schedule.getWriter().getId())
                        .build();
            default:
                throw new RuntimeException("bad request. not matched schedule type");
        }
    }
}
