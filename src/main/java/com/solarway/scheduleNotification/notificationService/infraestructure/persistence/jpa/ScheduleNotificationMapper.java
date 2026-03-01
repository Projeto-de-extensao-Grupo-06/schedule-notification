package com.solarway.scheduleNotification.notificationService.infraestructure.persistence.jpa;

import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;

public class ScheduleNotificationMapper {
    private ScheduleNotificationMapper() {}

    public static ScheduleNotificationEntity toEntity(ScheduleNotification domain) {
        ScheduleNotificationEntity entity = new ScheduleNotificationEntity();
        entity.setId(domain.getId());
        entity.setScheduleId(domain.getScheduleId());
        entity.setProjectTitle(domain.getProjectTitle());
        entity.setEmail(domain.getContato().getEmail().getValue());
        entity.setPhone(domain.getContato().getPhone().getValue());
        entity.setType(domain.getType());
        entity.setStatus(domain.getStatus());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setSendAt(domain.getSendAt().getValue());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    public static ScheduleNotification toDomain(ScheduleNotificationEntity entity) {
        return ScheduleNotification.existing(
                entity.getId(),
                entity.getScheduleId(),
                entity.getProjectTitle(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getType(),
                entity.getStatus(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getSendAt(),
                entity.getCreatedAt()
        );
    }
}
