package com.solarway.scheduleNotification.notificationService.core.domain.shared.vo;

import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.InvalidArgumentException;

public class Phone {

    private String value;

    private Phone(String value) {
        this.value = value;
    }

    public static Phone of(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidArgumentException("Telefone não pode ser vazio.");
        }
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.length() < 10 || digits.length() > 13) {
            throw new InvalidArgumentException("Telefone inválido: " + value);
        }
        return new Phone(digits);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

}
