package Capstone.Aeroponics.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

import Capstone.Aeroponics.models.request.UserRO;
import Capstone.Aeroponics.services.UserService;
import Capstone.Aeroponics.utils.MessageUtils;
import Capstone.Aeroponics.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userServices;

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
    public ResponseEntity<?> getByid(@PathVariable int id) {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(userServices.USER),
                userServices.getUserById(id)
            )
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
        return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                        HttpStatus.OK,
                        MessageUtils.retrieveSuccessMessage(userServices.USER),
                        userServices.getProfile(request)
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody UserRO userRO) {
        userServices.save(userRO);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.saveSuccessMessage(userServices.USER)
            )
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody UserRO userRO) {
        userServices.update(id, userRO);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.saveSuccessMessage(userServices.USER)
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        userServices.delete(id);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.deleteSuccessMessage(userServices.USER)
            )
        );
    }

    // @PostMapping("/login")
    // public ResponseEntity<String> login(@RequestBody UserRO userRO){
    //     try {
    //         userServices.login(userRO);
    //         return ResponseEntity.ok("User logged in successfully");
    //     } catch (Exception e) {
    //         return ResponseEntity.status(401).body(e.getMessage());
    //     }
    // }
}