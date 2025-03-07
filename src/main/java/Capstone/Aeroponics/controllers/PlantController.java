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
import org.springframework.web.bind.annotation.RestController;

import Capstone.Aeroponics.models.RO.PlantRO;
import Capstone.Aeroponics.services.PlantService;
import Capstone.Aeroponics.utils.MessageUtils;
import Capstone.Aeroponics.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plant")
@RequiredArgsConstructor
public class PlantController {
    
    private final PlantService plantServices;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(plantServices.PLANTS),
                plantServices.getall()
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getByid(@PathVariable Long id) {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(plantServices.PLANT),
                plantServices.getPlantById(id)
            )
        );
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody PlantRO plantRO) {
        plantServices.save(plantRO);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.saveSuccessMessage(plantServices.PLANT)
            )
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PlantRO plantRO) {
        plantServices.update(id, plantRO);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.saveSuccessMessage(plantServices.PLANT)
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        plantServices.delete(id);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.deleteSuccessMessage(plantServices.PLANT)
            )
        );
    }
}
