package Capstone.Aeroponics.controllers;

import Capstone.Aeroponics.models.request.NutrientRO;
import Capstone.Aeroponics.services.NutrientService;
import Capstone.Aeroponics.utils.MessageUtils;
import Capstone.Aeroponics.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nutrient")
@RequiredArgsConstructor
public class NutrientController {

    private final NutrientService nutrientService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(nutrientService.NUTRIENTS),
                nutrientService.getAll()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getByid(@PathVariable long id) {
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.retrieveSuccessMessage(nutrientService.NUTRIENT),
                        nutrientService.getNutrientById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody NutrientRO nutrientRO) {
        nutrientService.save(nutrientRO);
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.saveSuccessMessage(nutrientService.NUTRIENT)
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody NutrientRO nutrientRO) {
        nutrientService.update(id, nutrientRO);
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.saveSuccessMessage(nutrientService.NUTRIENT)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        nutrientService.delete(id);
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.deleteSuccessMessage(nutrientService.NUTRIENT)
                )
        );
    }
}
