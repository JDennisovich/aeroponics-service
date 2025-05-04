package Capstone.Aeroponics.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionType {

    SCOPE_READ("READ"),
    SCOPE_WRITE("WRITE"),
    SCOPE_UPDATE("UPDATE"),
    SCOPE_DELETE("DELETE");

    private final String permission;
}
