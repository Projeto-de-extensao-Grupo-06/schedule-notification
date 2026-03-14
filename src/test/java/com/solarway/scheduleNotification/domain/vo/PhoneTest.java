package com.solarway.scheduleNotification.domain.vo;

import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.InvalidArgumentException;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.vo.Phone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhoneTest {

    @Test
    @DisplayName("Deve criar Phone válido removendo caracteres não numéricos")
    void ofShouldCreatePhoneKeepingOnlyDigits(){
        Phone phone = Phone.of("(11) 99999-9999");

        assertEquals("11999999999", phone.getValue());

    }

    @Test
    @DisplayName("Deve aceitar telefone com código internacional")
    void ofShouldAcceptPhoneWithCountryCode(){
        Phone phone = Phone.of("+5511999999999");

        assertEquals("5511999999999", phone.getValue());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidPhoneProvider")
    @DisplayName("Deve lançar InvalidArgumentException para telefone inválidos")
    void  ofShouldThrowWhenPhoneIsInvalid(String description, String value){
        assertThrows(InvalidArgumentException.class, () -> Phone.of(value));
    }

    static Stream<Arguments> invalidPhoneProvider() {
        return Stream.of(
                Arguments.of("Deve lançar quando telefone é nulo", null),
                Arguments.of("Deve lançar quando telefone é vazio", ""),
                Arguments.of("Deve lançar quando telefone é apenas espaços", "   "),
                Arguments.of("Deve lançar quando telefone tem menos de 10 dígitos", "119999"),
                Arguments.of("Deve lançar quando telefone tem mais de 13 dígitos", "5511999999999999")
        );
    }


}
