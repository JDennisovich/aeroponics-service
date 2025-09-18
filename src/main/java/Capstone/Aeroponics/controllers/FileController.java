package Capstone.Aeroponics.controllers;

import Capstone.Aeroponics.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class FileController{

    @Autowired
    private FileService fileService;

    @GetMapping("/image/{imageName}")
    public ResponseEntity<?> getImage(@PathVariable String imageName) {
        Path result = fileService.getImage(imageName);
        byte[] image = fileService.getImageByte(result);

        Map<String, Object> response = new HashMap<>();

        if (Objects.isNull(result) || Objects.isNull(image)) {
            response.put("status", false);
            response.put("message", "Image Error");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }

        if (image.length == 0) {
            response.put("status", true);
            response.put("message", "Image does not exist");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }

        String contentType = fileService.getImageContentType(result);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(contentType))
                .body(image);
    }
}

