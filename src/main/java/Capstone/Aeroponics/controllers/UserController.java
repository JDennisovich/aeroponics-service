package Capstone.Aeroponics.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import Capstone.Aeroponics.exception.ServiceException;
import Capstone.Aeroponics.models.request.UserRO;
import Capstone.Aeroponics.services.UserService;
import Capstone.Aeroponics.utils.MessageUtils;
import Capstone.Aeroponics.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

    @GetMapping("/{id:\\d+}")
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

    @PostMapping("/profile-picture")
    public ResponseEntity<?> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        try {
            String fileUrl = userServices.uploadProfilePicture(file, request);
            return ResponseEntity.ok(
                ResponseUtils.buildSuccessResponse(
                    HttpStatus.OK,
                    "Profile picture uploaded successfully",
                    Map.of("url", fileUrl)
                )
            );
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(
                ResponseUtils.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseUtils.buildErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to upload profile picture: " + e.getMessage()
                )
            );
        }
    }

    @GetMapping("/profile-picture")
    public ResponseEntity<?> getProfilePicture(HttpServletRequest request) {
        try {
            UserService.FileData file = userServices.getProfilePictureFile(request);
            if (file == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseUtils.buildErrorResponse(
                        HttpStatus.NOT_FOUND,
                        "Profile picture not set or not found"
                    )
                );
            }
            return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(file.contentType != null ? file.contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(file.bytes);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(
                ResponseUtils.buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseUtils.buildErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch profile picture: " + e.getMessage()
                )
            );
        }
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