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

import Capstone.Aeroponics.models.request.PlantRO;
import Capstone.Aeroponics.services.PlantService;
import Capstone.Aeroponics.utils.MessageUtils;
import Capstone.Aeroponics.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
                plantServices.getAll()
            )
        );
    }

    @GetMapping("/names")
    public ResponseEntity<?> getAllNames() {
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.retrieveSuccessMessage(plantServices.PLANTS),
                        plantServices.getAllPlantNames()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getByid(@PathVariable long id) {
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

    @PostMapping("/bulk")
    public ResponseEntity<?> savePlants(@RequestBody List<PlantRO> plantROList) {
        plantServices.saveAll(plantROList);
        return ResponseEntity.ok("Plants saved successfully");
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getPlantsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.retrieveSuccessMessage(plantServices.PLANTS),
                        plantServices.getPlantsByUserId(id)
                )
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody PlantRO plantRO) {
        plantServices.update(id, plantRO);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.saveSuccessMessage(plantServices.PLANT)
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        plantServices.delete(id);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.deleteSuccessMessage(plantServices.PLANT)
            )
        );
    }
}
