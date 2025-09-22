package Capstone.Aeroponics.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum WaterLevel {

    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    private final String label;

    @Override
    public String toString() {
        return label;
    }
}
