package com.solarway.scheduleNotification.notificationService.infraestructure.email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NoteNotificationEmail {
    private String to;
    private String subject;
    private String coworkerName;
    private String title;
    private String date;
    private String hour;
}
