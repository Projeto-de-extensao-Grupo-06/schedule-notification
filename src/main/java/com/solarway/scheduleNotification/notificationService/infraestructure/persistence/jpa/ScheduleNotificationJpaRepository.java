package com.solarway.scheduleNotification.notificationService.infraestructure.persistence.jpa;

import com.solarway.scheduleNotification.notificationService.core.domain.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleNotificationJpaRepository extends JpaRepository<ScheduleNotificationEntity, Long> {

    Optional<ScheduleNotificationEntity> findByScheduleId(Long scheduleId);

    List<ScheduleNotificationEntity> findAllByStatusAndSendAtLessThanEqual(
            NotificationStatus status,
            LocalDateTime now
    );

    @Transactional
    void deleteByScheduleId(Long scheduleId);
}
