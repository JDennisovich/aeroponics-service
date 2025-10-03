package Capstone.Aeroponics.controllers;

import Capstone.Aeroponics.models.entities.Device;
import Capstone.Aeroponics.models.request.DeviceRO;
import Capstone.Aeroponics.services.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    // NodeMCU sends JSON with MAC + IP to backend
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerDevice(@Valid @RequestBody DeviceRO request) {
        try {
            Device device = deviceService.registerDevice(request.mac_address(), request.ip_address());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Device registered successfully");
            response.put("device", device);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to register device: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    // (Optional) Get first free device
    @GetMapping("/free")
    public ResponseEntity<Device> getFreeDevice() {
        Device device = deviceService.findFreeDevice();
        if (device == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(device);
    }
}