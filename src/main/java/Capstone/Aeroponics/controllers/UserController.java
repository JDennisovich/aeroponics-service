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

import Capstone.Aeroponics.models.RO.UserRO;
import Capstone.Aeroponics.services.UserServices;
import Capstone.Aeroponics.utils.MessageUtils;
import Capstone.Aeroponics.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServices userServices;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(userServices.USERS),
                userServices.getall()
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getByid(@PathVariable Long id) {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(userServices.USER),
                userServices.getUserById(id)
            )
        );
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserRO userRO) {
        userServices.save(userRO);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.saveSuccessMessage(userServices.USER)
            )
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserRO userRO) {
        userServices.update(id, userRO);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.saveSuccessMessage(userServices.USER)
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userServices.delete(id);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.deleteSuccessMessage(userServices.USER)
            )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRO userRO){
        try {
            userServices.login(userRO);
            return ResponseEntity.ok("User logged in successfully");
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}