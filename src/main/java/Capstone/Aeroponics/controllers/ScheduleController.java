package Capstone.Aeroponics.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Capstone.Aeroponics.models.request.ScheduleRO;
import Capstone.Aeroponics.services.ScheduleService;
import Capstone.Aeroponics.utils.MessageUtils;
import Capstone.Aeroponics.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    
    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(scheduleService.SCHEDULES),
                scheduleService.getAll()
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(scheduleService.SCHEDULE),
                scheduleService.getScheduleById(id)
            )
        );
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ScheduleRO scheduleRO, @RequestParam Long towerId) {
        scheduleService.save(scheduleRO, towerId);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.saveSuccessMessage(scheduleService.SCHEDULE)
            )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody ScheduleRO scheduleRO, @RequestParam Long towerId) {
        scheduleService.update(id, scheduleRO, towerId);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.saveSuccessMessage(scheduleService.SCHEDULE)
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.deleteSuccessMessage(scheduleService.SCHEDULE)
            )
        );
    }
}
