package Capstone.Aeroponics.models.request;

import Capstone.Aeroponics.models.entities.Device;
import Capstone.Aeroponics.models.entities.Tower;
import Capstone.Aeroponics.models.enums.DeviceStatus;
import jakarta.validation.constraints.NotBlank;

public record DeviceRO(
        Long id,
        @NotBlank(message = "MAC address is mandatory") String mac_address,
        String ip_address,
        DeviceStatus status,
        Long tower_id
) {
    public Device toEntity(Device device, Tower tower) {
        if (device == null) {
            device = new Device();
        }
        device.setMacAddress(mac_address);
        device.setIpAddress(ip_address);
        if (status != null) {
            device.setStatus(status);
        }
        device.setTower(tower);

        return device;
    }
}
