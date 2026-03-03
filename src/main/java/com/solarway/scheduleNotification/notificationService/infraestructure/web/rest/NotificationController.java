package com.solarway.scheduleNotification.notificationService.infraestructure.web.rest;

import com.solarway.scheduleNotification.notificationService.core.application.command.CancelNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.application.command.CreateNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.application.command.UpdateNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.CancelNotificationUseCase;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.CreateNotificationUseCase;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.UpdateNotificationUseCase;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

     private final CreateNotificationUseCase createUseCase;
     private final UpdateNotificationUseCase updateUseCase;
     private final CancelNotificationUseCase cancelUseCase;

    public NotificationController(CreateNotificationUseCase createUseCase, UpdateNotificationUseCase updateUseCase, CancelNotificationUseCase cancelUseCase) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.cancelUseCase = cancelUseCase;
    }

    @PostMapping
    public ResponseEntity<ScheduleNotification> create(@RequestBody CreateNotificationCommand command){
        ScheduleNotification notification = createUseCase.execute(command);
        return ResponseEntity.status(201).body(notification);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleNotification> update(
            @PathVariable long scheduleId,
            @RequestBody UpdateNotificationCommand command
            ) {
         ScheduleNotification notification = updateUseCase.execute(command);
         return ResponseEntity.ok(notification);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> cancel(@PathVariable Long scheduleId){
       cancelUseCase.execute(new CancelNotificationCommand(scheduleId));
       return ResponseEntity.noContent().build();
    }
}
