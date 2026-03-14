package com.solarway.scheduleNotification.notificationService.infraestructure.web.rest;

import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.InvalidArgumentException;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.NotificationNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.solarway.scheduleNotification.notificationService.infraestructure.web.rest.dto.ErrorResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.hibernate.NonUniqueResultException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotificationNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, ex.getMessage()));
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidArgument(InvalidArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, "Já existe uma notificação para este agendamento."));
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<ErrorResponse> handleNonUnique(NonUniqueResultException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, "Já existe uma notificação para este agendamento."));
    }
}
