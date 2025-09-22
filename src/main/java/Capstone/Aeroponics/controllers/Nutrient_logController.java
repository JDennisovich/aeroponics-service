package Capstone.Aeroponics.controllers;

import Capstone.Aeroponics.models.request.Nutrient_logsRO;
import Capstone.Aeroponics.services.Nutrient_logService;
import Capstone.Aeroponics.utils.MessageUtils;
import Capstone.Aeroponics.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nutrient")
@RequiredArgsConstructor
public class Nutrient_logController {

    private final Nutrient_logService nutrientLogService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(nutrientLogService.NUTRIENTS),
                nutrientLogService.getAll()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getByid(@PathVariable long id) {
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.retrieveSuccessMessage(nutrientLogService.NUTRIENT),
                        nutrientLogService.getNutrientById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Nutrient_logsRO nutrientLogsRO) {
        nutrientLogService.save(nutrientLogsRO);
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.saveSuccessMessage(nutrientLogService.NUTRIENT)
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody Nutrient_logsRO nutrientLogsRO) {
        nutrientLogService.update(id, nutrientLogsRO);
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.saveSuccessMessage(nutrientLogService.NUTRIENT)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        nutrientLogService.delete(id);
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.deleteSuccessMessage(nutrientLogService.NUTRIENT)
                )
        );
    }
}
