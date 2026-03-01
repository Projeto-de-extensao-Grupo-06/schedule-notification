package com.solarway.scheduleNotification.notificationService.core.domain.shared.vo;

public class Contato {
    private final Email email;
    private final Phone phone;

    private Contato(Email email, Phone phone) {
        this.email = email;
        this.phone = phone;
    }

    public static Contato of(String email, String phone) {
        return new Contato(
                Email.of(email),
                Phone.of(phone)
        );
    }

    public Email getEmail() {
        return email;
    }

    public Phone getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Contato{email=" + email + ", phone=" + phone + "}";
    }
}
