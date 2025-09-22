package de.opticoms.nms.lite.controller.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.opticoms.nms.lite.controller.model.EmergencyMessage;
import de.opticoms.nms.lite.controller.model.Message;
import de.opticoms.nms.lite.controller.model.OutputMessage;
import de.opticoms.nms.lite.data.model.MessageHistoryModel;
import de.opticoms.nms.lite.data.service.MessageHistoryService;
import de.opticoms.nms.lite.data.service.RegisteredSimService;
import de.opticoms.nms.lite.util.MessageType;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Controller
//@CrossOrigin("http://localhost:3000")
public class MessagingController {
    final SimpMessagingTemplate messagingTemplate;
    final RegisteredSimService registeredSimService;
    final MessageHistoryService messageHistoryService;

        private final ObjectMapper objectMapper = new ObjectMapper();

   @Autowired
    public MessagingController(SimpMessagingTemplate messagingTemplate,
                               RegisteredSimService registeredSimService,
                               MessageHistoryService messageHistoryService) {
        this.messagingTemplate = messagingTemplate;
        this.registeredSimService = registeredSimService;
        this.messageHistoryService = messageHistoryService;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload Message message) {
        val out = OutputMessage.builder()
                .from(message.getFrom())
                .text(message.getText())
                .type(MessageType.of("private").name())
                .time(new SimpleDateFormat("HH:mm").format(new Date()))
                .build();

        if (registeredSimService.existsByUsername(message.getTo())) {
            val savedMessage = messageHistoryService.save(MessageHistoryModel.builder()
                    .type(out.getType())
                    .from(message.getFrom())
                    .to(message.getTo())
                    .body(message.getText())
                    .seen(false)
                    .created(new Date(Instant.now().toEpochMilli()))
                    .build());

            log.info("Sending message from: {} to: {} with text: {} with id: {}", message.getFrom(), message.getTo(), message.getText(), out.getMessageHistoryId());

            out.setMessageHistoryId(savedMessage.getId());

            messagingTemplate.convertAndSendToUser(message.getTo(),
                    "/queue/private", out);
        }
    }

    /* TODO:
        how to subscribe to the channel
        client.subscribe('/topic/broadcast', (message) => handleReceivedMessage(message));
    */
    @MessageMapping("/broadcast")
    public void broadcastMessage(@Payload Message message) {
        val out = OutputMessage.builder()
                .from(message.getFrom())
                .text(message.getText())
                .type(MessageType.of("broadcast").name())
                .time(new SimpleDateFormat("HH:mm").format(new Date()))
                .build();

        messageHistoryService.save(MessageHistoryModel.builder()
                .type(out.getType())
                .from(message.getFrom())
                .body(message.getText())
                .created(new Date(Instant.now().toEpochMilli()))
                .build());

        log.info("Broadcasting message from: {} with text: {}", message.getFrom(), message.getText());

        messagingTemplate.convertAndSend("/topic/broadcast", out);
    }



    @MessageMapping("/emergency")
    public void emergency(@Payload Message message) {
        // Gönderen gerçekten kayıtlı mı? (private’da alıcıyı kontrol ediyorduk; emergency’de göndereni kontrol etmek mantıklı)
       
  if (!registeredSimService.existsByUsername(message.getFrom())) {
        log.warn("Emergency message rejected: unregistered sender '{}'", message.getFrom());
        return;
    }

        // UI’ye sade bir metin ve tip veriyoruz; ekstra alanlar JSON olarak body’de saklanacak
        val out = OutputMessage.builder()
                .from(message.getFrom())
                .text(message.getText()) // kısa mesaj
                .type(MessageType.of("emergency").name())
                .time(new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date()))
                .build();

        // DB’ye kaydedilecek body’yi zenginleştirelim: tüm emergency payload’ı JSON olarak sakla
            messageHistoryService.save(MessageHistoryModel.builder()
                .type(out.getType())
                .from(message.getFrom())
                .body(message.getText())
                .created(new Date(Instant.now().toEpochMilli()))
                .build());

        log.info("Broadcasting message from: {} with text: {}", message.getFrom(), message.getText());
        // Tüm dinleyicilere yayınla
        messagingTemplate.convertAndSend("/topic/emergency", out);
    }
}




