package com.solarway.scheduleNotification.notificationService.core.domain.shared.vo;

import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.InvalidArgumentException;

import java.util.regex.Pattern;

public class Email {
    private static final Pattern PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    public static Email of(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidArgumentException("Email não pode ser vazio.");
        }
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidArgumentException("Email inválido: " + value);
        }
        return new Email(value.toLowerCase().trim());
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
