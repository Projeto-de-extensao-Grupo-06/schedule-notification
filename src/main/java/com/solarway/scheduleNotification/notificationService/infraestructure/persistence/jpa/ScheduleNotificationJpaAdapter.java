package com.solarway.scheduleNotification.notificationService.infraestructure.persistence.jpa;

import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationMutation;
import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationQuery;

import com.solarway.scheduleNotification.notificationService.core.domain.model.NotificationStatus;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ScheduleNotificationJpaAdapter implements NotificationQuery, NotificationMutation {
    private final ScheduleNotificationJpaRepository repository;

    public ScheduleNotificationJpaAdapter(ScheduleNotificationJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ScheduleNotification> findByScheduleId(Long scheduleId) {
        return repository.findByScheduleId(scheduleId)
                .map(ScheduleNotificationMapper::toDomain);
    }

    @Override
    public List<ScheduleNotification> findAllPendingDue() {
        return repository
                .findAllByStatusAndSendAtLessThanEqual(NotificationStatus.PENDING, LocalDateTime.now())
                .stream()
                .map(ScheduleNotificationMapper::toDomain)
                .toList();
    }

    @Override
    public ScheduleNotification save(ScheduleNotification notification) {
        ScheduleNotificationEntity toSave = ScheduleNotificationMapper.toEntity(notification);
        ScheduleNotificationEntity saved = repository.save(toSave);
        return ScheduleNotificationMapper.toDomain(saved);
    }

    @Override
    public void deleteByScheduleId(Long scheduleId) {
        repository.deleteByScheduleId(scheduleId);
    }
}
