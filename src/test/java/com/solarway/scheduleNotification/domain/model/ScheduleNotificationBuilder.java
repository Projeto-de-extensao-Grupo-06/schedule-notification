package com.solarway.scheduleNotification.domain.model;

import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleType;

import java.time.LocalDateTime;

public class ScheduleNotificationBuilder {

    private Long scheduleId;
    private String projectTitle;
    private String title;
    private String email;
    private String phone;
    private ScheduleType type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int daysBefore;

    public static ScheduleNotificationBuilder builder(){
        return new ScheduleNotificationBuilder();
    }

    public ScheduleNotificationBuilder withScheduleId (Long scheduleId) {
        this.scheduleId = scheduleId;
        return this;
    }

    public ScheduleNotificationBuilder withProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
        return this;
    }

    public ScheduleNotificationBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ScheduleNotificationBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public ScheduleNotificationBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public ScheduleNotificationBuilder withType(ScheduleType type) {
        this.type = type;
        return this;
    }

    public ScheduleNotificationBuilder withStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public ScheduleNotificationBuilder withEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public ScheduleNotificationBuilder withDaysBefore(int daysBefore) {
        this.daysBefore = daysBefore;
        return this;
    }

    public ScheduleNotification build() {
        return ScheduleNotification.newSchedule(
                scheduleId,
                projectTitle,
                title,
                email,
                phone,
                type,
                startDate,
                endDate,
                daysBefore
        );
    }
}
