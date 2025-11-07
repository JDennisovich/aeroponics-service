package Capstone.Aeroponics.models.DTO.device;

import Capstone.Aeroponics.models.DTO.tower.TowerDTO;
import Capstone.Aeroponics.models.entities.Device;
import Capstone.Aeroponics.models.enums.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {

    private Long id;
    private String macAddress;
    private DeviceStatus status;
    private TowerDTO tower;

    public DeviceDTO(Device device) {
        this.id = device.getId();
        this.macAddress = device.getMacAddress();
        this.status = device.getStatus();
        
        if (device.getTower() != null) {
            this.tower = new TowerDTO(device.getTower());
        }
    }
}
