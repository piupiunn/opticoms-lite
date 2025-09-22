package de.opticoms.nms.lite.controller.socket.interceptor;

import de.opticoms.nms.lite.data.service.RegisteredSimService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
@Component
public class SubscriptionInterceptor implements ChannelInterceptor {

    final RegisteredSimService registeredSimService;

    @Autowired
    public SubscriptionInterceptor(RegisteredSimService registeredSimService) {
        this.registeredSimService = registeredSimService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

            if (Objects.nonNull(accessor) && SimpMessageType.SUBSCRIBE.equals(accessor.getMessageType())) {
                val destination = accessor.getDestination();

                if (Objects.nonNull(destination) && destination.startsWith("/user/")) {
                    val username = destination.split("/")[2];

                    if (!registeredSimService.existsByUsername(username)) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + username + " does not exist");
                    }
                    log.info("User {} is subscribing to: {}", username, destination);
                }
            }
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return message;
    }
}
