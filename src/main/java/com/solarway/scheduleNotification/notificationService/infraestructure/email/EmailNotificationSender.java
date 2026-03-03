package com.solarway.scheduleNotification.notificationService.infraestructure.email;

import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationSender;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailNotificationSender implements NotificationSender {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationSender.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final JavaMailSender mailSender;
    private final String from;

    public EmailNotificationSender(JavaMailSender mailSender, String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    @Override
    public void send(ScheduleNotification notification) {
        VisitNotificationEmail model = VisitNotificationEmail.builder()
                .to(notification.getContato().getEmail().getValue())
                .subject("Lembrete de Visita - Solarize")
                .clientName(notification.getProjectTitle())
                .date(notification.getStartDate().format(DATE_FORMATTER))
                .hour(notification.getStartDate().format(HOUR_FORMATTER))
                .address("Ver detalhes no sistema")
                .visitType(notification.getType().name().replace("_", " "))
                .build();

        String body = buildTemplate(model);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress(from));
            message.setRecipients(MimeMessage.RecipientType.TO, model.getTo());
            message.setSubject(model.getSubject());
            message.setContent(body, "text/html; charset=utf-8");
            mailSender.send(message);
            log.info("Email enviado para {} | scheduleId={}", model.getTo(), notification.getScheduleId());
        } catch (MessagingException e) {
            throw new RuntimeException(
                    String.format("Falha ao enviar email para '%s': %s", model.getTo(), e.getMessage())
            );
        }
    }

    private String buildTemplate(VisitNotificationEmail model) {
        try {
            InputStream resource = getClass().getResourceAsStream("/template/visitNotificationTemplate.html");

            if (resource == null) {
                throw new FileNotFoundException("Template não encontrado: visitNotificationTemplate.html");
            }

            String template = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
            resource.close();

            return template
                    .replace("${client_name}", model.getClientName())
                    .replace("${date}", model.getDate())
                    .replace("${hour}", model.getHour())
                    .replace("${address}", model.getAddress())
                    .replace("${vitit_type}", model.getVisitType());

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar template de email: " + e.getMessage());
        }
    }
}
