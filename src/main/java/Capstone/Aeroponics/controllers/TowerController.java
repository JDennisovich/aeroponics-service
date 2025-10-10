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

import Capstone.Aeroponics.models.DTO.tower.TowerDTO;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.models.request.TowerRO;
import Capstone.Aeroponics.services.TowerService;
import Capstone.Aeroponics.utils.MessageUtils;
import Capstone.Aeroponics.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tower")
@RequiredArgsConstructor
public class TowerController {

    private final TowerService towerService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(towerService.TOWERS),
                towerService.getAll()
            )
        );
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getTowerByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.retrieveSuccessMessage(towerService.TOWER),
                        towerService.getTowersByUserId(id)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(towerService.TOWER),
                towerService.getTowerByIdDTO(id)
            )
        );
    }

    @GetMapping("/{id}/phLevel")
    public ResponseEntity<?> getPhLevelById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.retrieveSuccessMessage("Ph Level"),
                        towerService.getTowerPhLevels(id)
                )
        );
    }

    @GetMapping("/{id}/ppm")
    public ResponseEntity<?> getPpmById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.retrieveSuccessMessage("PPM"),
                        towerService.getTowerPpms(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TowerRO towerRO) {
        Tower savedTower = towerService.save(towerRO);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.saveSuccessMessage(TowerService.TOWER),
                new TowerDTO(savedTower)
            )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TowerRO towerRO) {
        towerService.update(id, towerRO);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.saveSuccessMessage(towerService.TOWER)
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        towerService.delete(id);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.deleteSuccessMessage(towerService.TOWER)
            )
        );
    }

    // Assign a device to a tower
    @PostMapping("/{towerId}/assign-device/{deviceId}")
    public ResponseEntity<?> assignDevice(@PathVariable Long towerId, @PathVariable Long deviceId) {
        towerService.assignDeviceToTower(towerId, deviceId);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                "Device assigned to tower successfully"
            )
        );
    }

    // Get device assigned to a tower
    @GetMapping("/{towerId}/device")
    public ResponseEntity<?> getDeviceByTowerId(@PathVariable Long towerId) {
        try {
            return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                    HttpStatus.OK,
                    "Successfully retrieved device for tower",
                    towerService.getDeviceByTowerId(towerId)
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtils.buildErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error retrieving devices: " + e.getMessage()
                ));
        }
    }
}