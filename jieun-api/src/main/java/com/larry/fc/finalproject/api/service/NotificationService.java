package com.larry.fc.finalproject.api.service;

import com.larry.fc.finalproject.api.dto.AuthUser;
import com.larry.fc.finalproject.api.dto.NotificationCreateReq;
import com.larry.fc.finalproject.core.domain.entity.Schedule;
import com.larry.fc.finalproject.core.domain.entity.User;
import com.larry.fc.finalproject.core.domain.entity.repository.ScheduleRepository;
import com.larry.fc.finalproject.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserService userService;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void create(NotificationCreateReq notificationCreateReq, AuthUser authUser) {
        final User user = userService.findByUserId(authUser.getId());
        final List<LocalDateTime> notifyAtList = notificationCreateReq.getRepeatTimes();
        notifyAtList.forEach(notifyAt ->{
            final Schedule notificationSchedule =
                    Schedule.notification(
                            notificationCreateReq.getTitle(),
                            notifyAt,
                            user);
            scheduleRepository.save(notificationSchedule);
        });

    }
}
