package de.opticoms.nms.lite.util;

import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum UserRole {
    // db user roles
    AFAD_USER("1", "AFAD", "afad", "AFAD_USER", "afad_user", "afadUser"),
    PUBLIC_USER("2", "PUBLIC", "public", "PUBLIC_USER", "public_user", "publicUser");

    private final String[] aliases;

    private static final Map<String, UserRole> userRoles = new HashMap<>();

    static {
        Arrays.stream(UserRole.values())
                .forEach(sd -> Arrays.stream(sd.aliases)
                        .forEach(a -> userRoles.put(a, sd)));
    }

    UserRole(String... aliases) {
        this.aliases = aliases;
    }

    public static UserRole of(String alias) {
        if (StringUtils.isBlank(alias)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "UserRole cannot be empty");
        }

        val sd = userRoles.get(alias.toLowerCase());

        if (Objects.isNull(sd)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No enum alias for user role " + UserRole.class.getCanonicalName() + "." + alias);
        }
        return sd;
    }
}
