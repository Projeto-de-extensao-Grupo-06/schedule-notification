package com.solarway.scheduleNotification.notificationService.infraestructure.email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VisitNotificationEmail {
    private String to;
    private String subject;
    private String clientName;
    private String date;
    private String hour;
    private String address;
    private String visitType;

}
