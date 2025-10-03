package Capstone.Aeroponics.services;

import Capstone.Aeroponics.models.entities.Device;
import Capstone.Aeroponics.models.enums.DeviceStatus;
import Capstone.Aeroponics.repositories.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {

    private final DeviceRepository deviceRepository;

    // Register device as FREE (NodeMCU boot-up) - upsert logic
    public Device registerDevice(String mac_address, String ip_address) {
        Device device = deviceRepository.findByMacAddress(mac_address)
                .orElse(Device.builder()
                        .macAddress(mac_address)
                        .status(DeviceStatus.FREE)
                        .build());
        
        // Update IP address (always update to latest snapshot)
        device.setIpAddress(ip_address);
        
        return deviceRepository.save(device);
    }

    // Find first available FREE device
    public Device findFreeDevice() {
        return deviceRepository.findFirstByStatus(DeviceStatus.FREE).orElse(null);
    }

    // Assign device (used when linking to a Tower)
    public Device assignDevice(Device device) {
        device.setStatus(DeviceStatus.ASSIGNED);
        return deviceRepository.save(device);
    }
}