package com.solarway.scheduleNotification.notificationService.infraestructure.persistence.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.vo.Contato;

import java.util.List;

public class ScheduleNotificationMapper {
    private ScheduleNotificationMapper() {}

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ScheduleNotificationEntity toEntity(ScheduleNotification domain) {
        ScheduleNotificationEntity entity = new ScheduleNotificationEntity();
        entity.setId(domain.getId());
        entity.setScheduleId(domain.getScheduleId());
        entity.setProjectTitle(domain.getProjectTitle());
        entity.setTitle(domain.getTitle());
        entity.setRecipients(toJson(domain.getRecipients()));
        entity.setType(domain.getType());
        entity.setStatus(domain.getStatus());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setSendAt(domain.getSendAt().getValue());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    public static ScheduleNotification toDomain(ScheduleNotificationEntity entity) {
        return ScheduleNotification.existing(
                entity.getId(),
                entity.getScheduleId(),
                entity.getProjectTitle(),
                entity.getTitle(),
                fromJson(entity.getRecipients()),
                entity.getType(),
                entity.getStatus(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getSendAt(),
                entity.getCreatedAt()
        );
    }
    private static String toJson(List<Contato> recipients) {
        try {
            List<ContatoJson> list = recipients.stream()
                    .map(contato -> new ContatoJson(contato.getEmail().getValue(), contato.getPhone().getValue()))
                    .toList();
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter destinatários para JSON", e);
        }
    }

    private static List<Contato> fromJson(String json) {
        try {
            List<ContatoJson> list = objectMapper.readValue(json, new TypeReference<>() {});
            return list.stream()
                    .map(contato -> Contato.of(contato.email(), contato.phone()))
                    .toList();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter JSON para destinatários", e);
        }
    }

    private record ContatoJson(String email, String phone) {}
}
