package de.opticoms.nms.lite.util;

import de.opticoms.nms.lite.controller.model.Message;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum MessageType {
    // db user roles
    PRIVATE("1", "private"),
    BROADCAST("2", "broadcast"),
    EMERGENCY("3", "emergency"); // ✅ new type
   

    private final String[] aliases;

    private static final Map<String, MessageType> messageTypes = new HashMap<>();

    static {
        Arrays.stream(MessageType.values())
                .forEach(sd -> Arrays.stream(sd.aliases)
                        .forEach(a -> messageTypes.put(a, sd)));
    }

    MessageType(String... aliases) {
        this.aliases = aliases;
    }

    public static MessageType of(String alias) {
        if (StringUtils.isBlank(alias)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "MessageType cannot be empty");
        }

        val sd = messageTypes.get(alias.toLowerCase());

        if (Objects.isNull(sd)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No enum alias for message type " + MessageType.class.getCanonicalName() + "." + alias);
        }
        return sd;
    }
}
