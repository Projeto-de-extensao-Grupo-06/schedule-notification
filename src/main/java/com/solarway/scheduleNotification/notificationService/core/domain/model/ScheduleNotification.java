package com.solarway.scheduleNotification.notificationService.core.domain.model;

import com.solarway.scheduleNotification.notificationService.core.domain.shared.vo.Contato;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.vo.SendAt;

import java.time.LocalDateTime;

public class ScheduleNotification {

    private Long id;
    private Long scheduleId;
    private String projectTitle;
    private String title;
    private Contato contato;
    private SendAt sendAt;
    private ScheduleType type;
    private NotificationStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;

    private ScheduleNotification(
            Long id,
            Long scheduleId,
            String projectTitle,
            String title,
            Contato contato,
            SendAt sendAt,
            ScheduleType type,
            NotificationStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.projectTitle = projectTitle;
        this.title = title;
        this.contato = contato;
        this.sendAt = sendAt;
        this.type = type;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
    }

    public static ScheduleNotification newSchedule(
            Long scheduleId,
            String projectTitle,
            String title,
            String email,
            String phone,
            ScheduleType type,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int daysBefore
    ) {
        return new ScheduleNotification(
                null,
                scheduleId,
                projectTitle,
                title,
                Contato.of(email, phone),
                SendAt.of(startDate, daysBefore),
                type,
                NotificationStatus.PENDING,
                startDate,
                endDate,
                LocalDateTime.now()
        );
    }

    public static ScheduleNotification existing(
            Long id,
            Long scheduleId,
            String projectTitle,
            String title,
            String email,
            String phone,
            ScheduleType type,
            NotificationStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime sendAt,
            LocalDateTime createdAt
    ) {
        return new ScheduleNotification(
                id,
                scheduleId,
                projectTitle,
                title,
                Contato.of(email, phone),
                SendAt.of(sendAt, 0),
                type,
                status,
                startDate,
                endDate,
                createdAt
        );
    }


    public void markAsSent() {
        this.status = NotificationStatus.SENT;
    }

    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getScheduleId() { return scheduleId; }
    public String getProjectTitle() { return projectTitle; }
    public String getTitle() { return title; }
    public Contato getContato() { return contato; }
    public SendAt getSendAt() { return sendAt; }
    public ScheduleType getType() { return type; }
    public NotificationStatus getStatus() { return status; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "ScheduleNotification{" +
                "id=" + id +
                ", scheduleId=" + scheduleId +
                ", type=" + type +
                ", status=" + status +
                ", sendAt=" + sendAt +
                '}';
    }
}
