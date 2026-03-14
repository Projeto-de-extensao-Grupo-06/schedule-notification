package com.solarway.scheduleNotification.domain.vo;

import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.InvalidArgumentException;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.vo.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    @DisplayName("Deve criar Email válido com valor em lowercase")
    void ofShouldCreateValidEmailInLowerCase(){
        Email email = Email.of("Cliente@Email.COM");

        assertEquals("cliente@email.com", email.getValue());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidEmailProvider")
    @DisplayName("Deve lançar InvalidArgumentException para emails inválidos")
    void ofShouldThrowWhenEmailsIsInvalid(String description, String value){
        assertThrows(InvalidArgumentException.class, () -> Email.of(value));
    }

    static Stream<Arguments> invalidEmailProvider(){
        return Stream.of(
                Arguments.of("Deve lançar quando email é nulo", null),
                Arguments.of("Deve lançar quando email é vazio", ""),
                Arguments.of("Deve lançar quando email é apenas espaços", "   "),
                Arguments.of("Deve lançar quando email não tem @", "emailsemarroba.com"),
                Arguments.of("Deve lançar quando email não tem domínio", "email@"),
                Arguments.of("Deve lançar quando email não tem extensão válida","email@dominio")
        );
    }
}
