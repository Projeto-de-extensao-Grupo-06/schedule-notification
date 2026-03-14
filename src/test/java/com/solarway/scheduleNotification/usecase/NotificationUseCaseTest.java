package com.solarway.scheduleNotification.usecase;

import com.solarway.scheduleNotification.domain.model.ScheduleNotificationBuilder;
import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationMutation;
import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationQuery;
import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationSender;
import com.solarway.scheduleNotification.notificationService.core.application.command.CancelNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.application.command.CreateNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.CancelNotificationUseCase;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.CreateNotificationUseCase;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.SendPendingNotificationsUseCase;
import com.solarway.scheduleNotification.notificationService.core.domain.model.NotificationStatus;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleType;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.NotificationNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationUseCaseTest {

    private final NotificationMutation mutation = mock(NotificationMutation.class);
    private final NotificationQuery query = mock(NotificationQuery.class);
    private final NotificationSender sender = mock(NotificationSender.class);

    @Test
    @DisplayName("Deve criar notificação com status PENDING e persistir")
    void createShouldSaveNotificationWithPendingStatus() {
        CreateNotificationUseCase useCase = new CreateNotificationUseCase(mutation, 2);

        ScheduleNotification saved = ScheduleNotificationBuilder.builder()
                .withScheduleId(1L)
                .withProjectTitle("Projeto Solar")
                .withEmail("cliente@email.com")
                .withPhone("11999999999")
                .withType(ScheduleType.TECHNICAL_VISIT)
                .withStartDate(LocalDateTime.now().plusDays(5))
                .withEndDate(LocalDateTime.now().plusDays(5).plusHours(2))
                .withDaysBefore(2)
                .build();

        when(mutation.save(any())).thenReturn(saved);

        CreateNotificationCommand command = new CreateNotificationCommand(
                1L, "Projeto Solar", "cliente@email.com", "11999999999",
                ScheduleType.TECHNICAL_VISIT,
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(2)
        );

        ScheduleNotification result = useCase.execute(command);

        verify(mutation, times(1)).save(any());
        assertEquals(NotificationStatus.PENDING, result.getStatus());
    }

    @Test
    @DisplayName("Deve deletar notificação existente ao cancelar")
    void cancelShouldDeleteNotification() {
        CancelNotificationUseCase useCase = new CancelNotificationUseCase(query, mutation);

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

        when(query.findByScheduleId(1L)).thenReturn(Optional.of(notification));

        useCase.execute(new CancelNotificationCommand(1L));

        verify(mutation, times(1)).deleteByScheduleId(1L);
        verify(mutation, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar NotificationNotFoundException ao cancelar id inexistente")
    void cancelShouldThrowWhenNotificationNotFound() {
        CancelNotificationUseCase useCase = new CancelNotificationUseCase(query, mutation);

        when(query.findByScheduleId(99L)).thenReturn(Optional.empty());

        assertThrows(
                NotificationNotFoundException.class,
                () -> useCase.execute(new CancelNotificationCommand(99L))
        );

        verify(mutation, never()).deleteByScheduleId(any());
    }

    @Test
    @DisplayName("Deve marcar notificação como SENT após envio com sucesso")
    void sendPendingShouldMarkAsSentOnSuccess() {
        SendPendingNotificationsUseCase useCase = new SendPendingNotificationsUseCase(query, mutation, sender);

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

        when(query.findAllPendingDue()).thenReturn(List.of(notification));

        useCase.execute();

        assertEquals(NotificationStatus.SENT, notification.getStatus());
        verify(sender, times(1)).send(notification);
        verify(mutation, times(1)).save(notification);
    }

    @Test
    @DisplayName("Deve marcar notificação como FAILED quando envio lança exceção")
    void sendPendingShouldMarkAsFailedOnException() {
        SendPendingNotificationsUseCase useCase = new SendPendingNotificationsUseCase(query, mutation, sender);

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

        when(query.findAllPendingDue()).thenReturn(List.of(notification));
        doThrow(new RuntimeException("Falha no servidor de email")).when(sender).send(any());

        useCase.execute();

        assertEquals(NotificationStatus.FAILED, notification.getStatus());
        verify(mutation, times(1)).save(notification);
    }

    @Test
    @DisplayName("Deve processar todas as notificações pendentes mesmo que uma falhe")
    void sendPendingShouldProcessAllEvenIfOneFails() {
        SendPendingNotificationsUseCase useCase = new SendPendingNotificationsUseCase(query, mutation, sender);

        ScheduleNotification first = ScheduleNotificationBuilder.builder()
                .withScheduleId(1L)
                .withProjectTitle("Projeto Solar")
                .withEmail("cliente@email.com")
                .withPhone("11999999999")
                .withType(ScheduleType.TECHNICAL_VISIT)
                .withStartDate(LocalDateTime.now().plusDays(5))
                .withEndDate(LocalDateTime.now().plusDays(5).plusHours(2))
                .withDaysBefore(2)
                .build();

        ScheduleNotification second = ScheduleNotificationBuilder.builder()
                .withScheduleId(2L)
                .withProjectTitle("Projeto Solar 2")
                .withEmail("outro@email.com")
                .withPhone("11988888888")
                .withType(ScheduleType.INSTALL_VISIT)
                .withStartDate(LocalDateTime.now().plusDays(3))
                .withEndDate(LocalDateTime.now().plusDays(3).plusHours(2))
                .withDaysBefore(2)
                .build();

        when(query.findAllPendingDue()).thenReturn(List.of(first, second));
        doThrow(new RuntimeException("Erro")).when(sender).send(first);

        useCase.execute();

        assertEquals(NotificationStatus.FAILED, first.getStatus());
        assertEquals(NotificationStatus.SENT, second.getStatus());
        verify(mutation, times(2)).save(any());
    }

    @Test
    @DisplayName("Não deve chamar sender nem mutation quando não há notificações pendentes")
    void sendPendingShouldDoNothingWhenNoPendingNotifications() {
        SendPendingNotificationsUseCase useCase = new SendPendingNotificationsUseCase(query, mutation, sender);

        when(query.findAllPendingDue()).thenReturn(List.of());

        useCase.execute();

        verify(sender, never()).send(any());
        verify(mutation, never()).save(any());
    }
}
