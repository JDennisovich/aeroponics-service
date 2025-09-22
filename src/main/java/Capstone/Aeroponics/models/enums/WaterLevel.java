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
}
