package com.solarway.scheduleNotification.notificationService.core.domain.model;

public enum ScheduleType {
    TECHNICAL_VISIT("Visita Técnica"),
    INSTALL_VISIT("Visita de Instalação"),
    NOTE("Observação");

    public final String label;

    ScheduleType(String label) {
        this.label = label;
    }
}
