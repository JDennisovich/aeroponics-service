package Capstone.Aeroponics.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static Capstone.Aeroponics.models.enums.PermissionType.SCOPE_DELETE;
import static Capstone.Aeroponics.models.enums.PermissionType.SCOPE_READ;
import static Capstone.Aeroponics.models.enums.PermissionType.SCOPE_UPDATE;
import static Capstone.Aeroponics.models.enums.PermissionType.SCOPE_WRITE;

@Getter
@RequiredArgsConstructor
public enum RoleType {

    ADMIN(
        Set.of(
            SCOPE_READ,
            SCOPE_WRITE,
            SCOPE_UPDATE,
            SCOPE_DELETE
        )
    ),
    BASIC(
        Set.of(
            SCOPE_READ,
            SCOPE_WRITE,
            SCOPE_UPDATE
        )
    ),
    TEST(
        Collections.emptySet()
    );

    private final Set<PermissionType> permissions;

    public List<String> getStringPermissions() {
        return getPermissions().stream().map(PermissionType::getPermission).toList();
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.name()))
            .toList();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
