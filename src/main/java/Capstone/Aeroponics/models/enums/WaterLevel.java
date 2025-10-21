package Capstone.Aeroponics.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum WaterLevel {

    HIGH("HIGH"),
    MEDIUM("MEDIUM"),
    LOW("LOW");

    private final String label;

    @Override
    public String toString() {
        return label;
    }

    /**
     * Convert integer water level to WaterLevel enum
     * @param level integer value representing water level
     * @return corresponding WaterLevel enum
     */
    public static WaterLevel fromInt(int level) {
        if (level >= 70) {
            return HIGH;
        } else if (level >= 30) {
            return MEDIUM;
        } else {
            return LOW;
        }
    }
}
