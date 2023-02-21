package com.larry.fc.finalproject.api.service;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.ScheduleDto;
import com.larry.fc.finalproject.api.util.DtoConverter;
import com.larry.fc.finalproject.core.domain.entity.Engagement;
import com.larry.fc.finalproject.core.domain.entity.Schedule;
import com.larry.fc.finalproject.core.domain.entity.repository.EngagementRepository;
import com.larry.fc.finalproject.core.domain.entity.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleQueryService {
    private final ScheduleRepository scheduleRepository;
    private final EngagementRepository engagementRepository;

    public List<ScheduleDto> getScheduleByDay(AuthUser authUser, LocalDate date) {
        return Stream.concat(
                        scheduleRepository.findAllByWriter_Id(authUser.getId())
                                .stream()
                                .filter(schedule -> schedule.isOverlapped(date)) //특정 date와 겹치는 것만 필터링을 한다
                                .map(schedule -> DtoConverter.fromSchedule(schedule)),
                        engagementRepository.findAllByAttendee_Id(authUser.getId())
                                .stream()
                                .filter(engagement -> engagement.isOverlapped(date))
                                .map(engagement -> DtoConverter.fromSchedule(engagement.getSchedule()))) //위 두 개 메서드 합칠 수 있다. concat
                .collect(Collectors.toList());
    }
}
