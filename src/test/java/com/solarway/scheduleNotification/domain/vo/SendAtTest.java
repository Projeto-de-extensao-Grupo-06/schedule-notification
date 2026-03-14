package com.solarway.scheduleNotification.domain.vo;

import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.InvalidArgumentException;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.vo.SendAt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SendAtTest {

    @Test
    @DisplayName("Deve calcular data de envio subtraindo daysBefore da startDate")
    void ofShouldSubtractDaysBeforeFromStartDate() {
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 10, 9, 0);

        SendAt sendAt = SendAt.of(startDate, 2);

        assertEquals(LocalDateTime.of(2025, 6, 8, 9, 0), sendAt.getValue());
    }

    @Test
    @DisplayName("Deve retornar a própria startDate quando daysBefore é zero")
    void ofShouldReturnStartDateWhenDaysBeforeIsZero() {
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 10, 9, 0);

        SendAt sendAt = SendAt.of(startDate, 0);

        assertEquals(startDate, sendAt.getValue());
    }

    @Test
    @DisplayName("Deve lançar InvalidArgumentException quando startDate é nulo")
    void ofShouldThrowWhenStartDateIsNull() {
        assertThrows(InvalidArgumentException.class, () -> SendAt.of(null, 2));
    }

    @Test
    @DisplayName("Deve lançar InvalidArgumentException quando daysBefore é negativo")
    void ofShouldThrowWhenDaysBeforeIsNegative() {
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 10, 9, 0);

        assertThrows(InvalidArgumentException.class, () -> SendAt.of(startDate, -1));
    }
}
