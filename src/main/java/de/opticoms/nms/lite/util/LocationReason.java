package de.opticoms.nms.lite.util;

import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum LocationReason {
    // db user roles
    EMERGENCY_MEDICAL_HELP("1", "SOS", "Emergency"),
    MEDICAL_SUPPLY_REQUEST("2", "Supply", "Medical Supply");

    private final String[] aliases;

    private static final Map<String, LocationReason> locationReasons = new HashMap<>();

    static {
        Arrays.stream(LocationReason.values())
                .forEach(sd -> Arrays.stream(sd.aliases)
                        .forEach(a -> locationReasons.put(a, sd)));
    }

    LocationReason(String... aliases) {
        this.aliases = aliases;
    }

    public static LocationReason of(String alias) {
        if (StringUtils.isBlank(alias)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "LocationReason cannot be empty");
        }

        val sd = locationReasons.get(alias.toLowerCase());

        if (Objects.isNull(sd)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No enum alias for location reason " + LocationReason.class.getCanonicalName() + "." + alias);
        }
        return sd;
    }
}
