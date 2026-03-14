package com.solarway.scheduleNotification.domain.model;

import com.solarway.scheduleNotification.notificationService.core.domain.model.NotificationStatus;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleNotificationTest {

    @Test
    @DisplayName("Deve criar notificação com status PENDING")
    void newScheduleShouldCreateWithPendingStatus() {
        ScheduleNotification notification = ScheduleNotificationBuilder.builder()
                .withScheduleId(1L)
                .withProjectTitle("Projeto Solar")
                .withEmail("cliente@email.com")
                .withPhone("11999999999")
                .withType(ScheduleType.TECHNICAL_VISIT)
                .withStartDate(LocalDateTime.now().plusDays(5))
                .withEndDate(LocalDateTime.now().plusDays(5).plusHours(2))
                .withDaysBefore(2)
                .build();

        assertEquals(NotificationStatus.PENDING, notification.getStatus());
    }

    @Test
    @DisplayName("Deve marcar notificação como SENT")
    void markAsSentShouldChangeStatusToSent() {
        ScheduleNotification notification = ScheduleNotificationBuilder.builder()
                .withScheduleId(1L)
                .withProjectTitle("Projeto Solar")
                .withEmail("cliente@email.com")
                .withPhone("11999999999")
                .withType(ScheduleType.TECHNICAL_VISIT)
                .withStartDate(LocalDateTime.now().plusDays(5))
                .withEndDate(LocalDateTime.now().plusDays(5).plusHours(2))
                .withDaysBefore(2)
                .build();

        notification.markAsSent();

        assertEquals(NotificationStatus.SENT, notification.getStatus());
    }

    @Test
    @DisplayName("Deve marcar notificação como FAILED")
    void markAsFailedShouldChangeStatusToFailed() {
        ScheduleNotification notification = ScheduleNotificationBuilder.builder()
                .withScheduleId(1L)
                .withProjectTitle("Projeto Solar")
                .withEmail("cliente@email.com")
                .withPhone("11999999999")
                .withType(ScheduleType.TECHNICAL_VISIT)
                .withStartDate(LocalDateTime.now().plusDays(5))
                .withEndDate(LocalDateTime.now().plusDays(5).plusHours(2))
                .withDaysBefore(2)
                .build();

        notification.markAsFailed();

        assertEquals(NotificationStatus.FAILED, notification.getStatus());
    }

    @Test
    @DisplayName("Deve calcular sendAt subtraindo daysBefore da startDate")
    void newScheduleShouldCalculateSendAtCorrectly() {
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 10, 9, 0);

        ScheduleNotification notification = ScheduleNotificationBuilder.builder()
                .withScheduleId(1L)
                .withProjectTitle("Projeto Solar")
                .withEmail("cliente@email.com")
                .withPhone("11999999999")
                .withType(ScheduleType.TECHNICAL_VISIT)
                .withStartDate(startDate)
                .withEndDate(startDate.plusHours(2))
                .withDaysBefore(2)
                .build();

        assertEquals(LocalDateTime.of(2025, 6, 8, 9, 0), notification.getSendAt().getValue());
    }

    static Stream<Arguments> cancelShouldNotChangeStatusWhenNotPendingProvider() {
        ScheduleNotification sent = ScheduleNotificationBuilder.builder()
                .withScheduleId(1L)
                .withProjectTitle("Projeto Solar")
                .withEmail("cliente@email.com")
                .withPhone("11999999999")
                .withType(ScheduleType.TECHNICAL_VISIT)
                .withStartDate(LocalDateTime.now().plusDays(5))
                .withEndDate(LocalDateTime.now().plusDays(5).plusHours(2))
                .withDaysBefore(2)
                .build();
        sent.markAsSent();

        ScheduleNotification failed = ScheduleNotificationBuilder.builder()
                .withScheduleId(2L)
                .withProjectTitle("Projeto Solar")
                .withEmail("cliente@email.com")
                .withPhone("11999999999")
                .withType(ScheduleType.TECHNICAL_VISIT)
                .withStartDate(LocalDateTime.now().plusDays(5))
                .withEndDate(LocalDateTime.now().plusDays(5).plusHours(2))
                .withDaysBefore(2)
                .build();
        failed.markAsFailed();

        return Stream.of(
                Arguments.of("Não deve alterar notificação já SENT", sent, NotificationStatus.SENT),
                Arguments.of("Não deve alterar notificação já FAILED", failed, NotificationStatus.FAILED)
        );
    }
}
