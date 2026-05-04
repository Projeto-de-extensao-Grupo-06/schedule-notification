package com.solarway.scheduleNotification.notificationService.infraestructure.email;

import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationSender;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleType;
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
        String to = notification.getContato().getEmail().getValue();
        String body;
        String subject;

        if (notification.getType() == ScheduleType.NOTE) {
            NoteNotificationEmail model = NoteNotificationEmail.builder()
                    .to(to)
                    .subject("Lembrete - Solarize")
                    .title(notification.getTitle())
                    .date(notification.getStartDate().format(DATE_FORMATTER))
                    .hour(notification.getStartDate().format(HOUR_FORMATTER))
                    .build();

            subject = model.getSubject();
            body = buildNoteTemplate(model);
        } else {
            VisitNotificationEmail model = VisitNotificationEmail.builder()
                    .to(to)
                    .subject("Lembrete de Visita - Solarize")
                    .clientName(notification.getProjectTitle())
                    .date(notification.getStartDate().format(DATE_FORMATTER))
                    .hour(notification.getStartDate().format(HOUR_FORMATTER))
                    .address("Ver detalhes no sistema")
                    .visitType(notification.getType().label)
                    .build();

            subject = model.getSubject();
            body = buildVisitTemplate(model);
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress(from));
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");
            mailSender.send(message);
            log.info("Email enviado para {} | scheduleId={}", to, notification.getScheduleId());
        } catch (MessagingException e) {
            throw new RuntimeException(
                    String.format("Falha ao enviar email para '%s': %s", to, e.getMessage())
            );
        }
    }

    private String buildVisitTemplate(VisitNotificationEmail model) {
        try {
            InputStream resource = getClass().getResourceAsStream("/templates/visitNotificationTemplate.html");

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

    private String buildNoteTemplate(NoteNotificationEmail model) {
        try {
            InputStream resource = getClass().getResourceAsStream("/templates/noteNotificationTemplate.html");

            if (resource == null) {
                throw new FileNotFoundException("Template não encontrado: noteNotificationTemplate.html");
            }

            String template = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
            resource.close();

            return template
                    .replace("${title}", model.getTitle() != null ? model.getTitle() : "Sem título")
                    .replace("${date}", model.getDate())
                    .replace("${hour}", model.getHour());

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar template de email: " + e.getMessage());
        }
    }
}
