package Capstone.Aeroponics.controllers;

import Capstone.Aeroponics.models.request.DeviceRO;
import Capstone.Aeroponics.services.DeviceService;
import Capstone.Aeroponics.services.UserService;
import Capstone.Aeroponics.utils.MessageUtils;
import Capstone.Aeroponics.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;
    private final UserService userService;

    // NodeMCU sends JSON with MAC + IP to backend
    @PostMapping("/register")
    public ResponseEntity<?> registerDevice(@Valid @RequestBody DeviceRO request) {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.saveSuccessMessage(deviceService.DEVICE),
                deviceService.registerDevice(request.mac_address(), request.user_id())
            )
        );
    }

    // (Optional) Get first free device
    @GetMapping("/free")
    public ResponseEntity<?> getFreeDevice() {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(deviceService.DEVICE),
                deviceService.findFreeDevice()
            )
        );
    }

    // Get all free devices - only devices assigned to the logged in user will be returned
    @GetMapping("/free/all")
    public ResponseEntity<?> getAllFreeDevices(HttpServletRequest request) {
        var user = userService.getUserProfile(request);
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(DeviceService.DEVICES),
                deviceService.getAllFreeDevices(user)
            )
        );
    }

    // Get device by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getDeviceById(@PathVariable Long id) {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(DeviceService.DEVICE),
                deviceService.getDeviceById(id)
            )
        );
    }

    // Get device by MAC address (for Arduino)
    @GetMapping("/mac/{macAddress}")
    public ResponseEntity<?> getDeviceByMacAddress(@PathVariable String macAddress) {
        return ResponseEntity.ok(
            ResponseUtils.buildSuccessResponse(
                HttpStatus.OK,
                MessageUtils.retrieveSuccessMessage(DeviceService.DEVICE),
                deviceService.getDeviceByMacAddress(macAddress)
            )
        );
    }
}