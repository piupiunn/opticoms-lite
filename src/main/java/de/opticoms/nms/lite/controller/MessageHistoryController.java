package de.opticoms.nms.lite.controller;

import de.opticoms.nms.lite.controller.model.MessageHistory;
import de.opticoms.nms.lite.controller.model.PagedData;
import de.opticoms.nms.lite.controller.model.UpdateSeen;
import de.opticoms.nms.lite.data.model.MessageHistoryModel;
import de.opticoms.nms.lite.data.service.MessageHistoryService;
import de.opticoms.nms.lite.data.service.RegisteredSimService;
import de.opticoms.nms.lite.data.util.GenericPagedModel;
import de.opticoms.nms.lite.util.SortDirection;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import static de.opticoms.nms.lite.data.util.Parsers.tryParseBoolean;
import static de.opticoms.nms.lite.data.util.Parsers.tryParseLong;

@Slf4j
@RestController
public class MessageHistoryController {
    final MessageHistoryService messageHistoryService;
    final RegisteredSimService registeredSimService;

    @Autowired
    public MessageHistoryController(MessageHistoryService messageHistoryService, RegisteredSimService registeredSimService) {
        this.messageHistoryService = messageHistoryService;
        this.registeredSimService = registeredSimService;
    }

    @RequestMapping(value = "/v1/messageHistory/find_messages", method = RequestMethod.GET)
    private ResponseEntity<PagedData<MessageHistory>>
    findAllByFromAndToAndSeenAndCreatedBeforeAndCreatedAfter
            (@RequestParam(defaultValue = "") String from,
             @RequestParam(defaultValue = "") String to,
             @RequestParam(defaultValue = "") String seen,
             @RequestParam String createdBefore,
             @RequestParam String createdAfter,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("Calling: findAllByFromAndToAndSeenAndCreatedBeforeAndCreatedAfter >> ");

        if (StringUtils.isNotEmpty(seen)) {
            if (StringUtils.isEmpty(from) && StringUtils.isNotEmpty(to)) {
                val response = messageHistoryService.findAllByToAndSeenAndCreatedBeforeAndCreatedAfter
                        (to, tryParseBoolean(seen, "seen"), new Date(tryParseLong(createdBefore, "createdBefore")),
                                new Date(tryParseLong(createdAfter, "createdAfter")),
                                pageNo, pageSize, sortBy, SortDirection.of(sortDir));
                return ResponseEntity.ok(mapPagedData(response));
            } else if (StringUtils.isEmpty(to) && StringUtils.isNotEmpty(from)) {
                val response = messageHistoryService.findAllByFromAndSeenAndCreatedBeforeAndCreatedAfter
                        (from, tryParseBoolean(seen, "seen"), new Date(tryParseLong(createdBefore, "createdBefore")),
                                new Date(tryParseLong(createdAfter, "createdAfter")),
                                pageNo, pageSize, sortBy, SortDirection.of(sortDir));
                return ResponseEntity.ok(mapPagedData(response));
            } else if (StringUtils.isNotEmpty(to) && StringUtils.isNotEmpty(from)) {
                val response = messageHistoryService.findAllByFromAndToAndSeenAndCreatedBeforeAndCreatedAfter
                        (from, to, tryParseBoolean(seen, "seen"), new Date(tryParseLong(createdBefore, "createdBefore")),
                                new Date(tryParseLong(createdAfter, "createdAfter")),
                                pageNo, pageSize, sortBy, SortDirection.of(sortDir));
                return ResponseEntity.ok(mapPagedData(response));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least from or to must be provided");
            }
        } else {
            if (StringUtils.isEmpty(from) && StringUtils.isNotEmpty(to)) {
                val response = messageHistoryService.findAllByToAndCreatedBeforeAndCreatedAfter
                        (to, new Date(tryParseLong(createdBefore, "createdBefore")),
                                new Date(tryParseLong(createdAfter, "createdAfter")),
                                pageNo, pageSize, sortBy, SortDirection.of(sortDir));
                return ResponseEntity.ok(mapPagedData(response));
            } else if (StringUtils.isEmpty(to) && StringUtils.isNotEmpty(from)) {
                val response = messageHistoryService.findAllByFromAndCreatedBeforeAndCreatedAfter
                        (from, new Date(tryParseLong(createdBefore, "createdBefore")),
                                new Date(tryParseLong(createdAfter, "createdAfter")),
                                pageNo, pageSize, sortBy, SortDirection.of(sortDir));
                return ResponseEntity.ok(mapPagedData(response));
            } else if (StringUtils.isNotEmpty(to) && StringUtils.isNotEmpty(from)) {
                val response = messageHistoryService.findAllByFromAndToAndCreatedBeforeAndCreatedAfter
                        (from, to, new Date(tryParseLong(createdBefore, "createdBefore")),
                                new Date(tryParseLong(createdAfter, "createdAfter")),
                                pageNo, pageSize, sortBy, SortDirection.of(sortDir));
                return ResponseEntity.ok(mapPagedData(response));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least from or to must be provided");
            }
        }
    }


    @RequestMapping(value = "/v1/messageHistory/update_seen", method = RequestMethod.PUT)
    private ResponseEntity<MessageHistory> updateMessageHistory(@RequestBody UpdateSeen updateSeen) {
        log.info("Calling: updateMessageHistory >> ".concat(updateSeen.getMessageHistoryId().toString()));

        if (registeredSimService.existsByUsername(updateSeen.getTo())) {
            val message = messageHistoryService.findById(updateSeen.getMessageHistoryId());
            message.setSeen(updateSeen.getSeen());
            val response = messageHistoryService.save(message);

            return ResponseEntity.ok(mapMessageHistory(response));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User: " + updateSeen.getTo() + " does not exist");
        }
    }

    private MessageHistory mapMessageHistory(MessageHistoryModel messageHistoryModel) {
        return MessageHistory.builder()
                .id(messageHistoryModel.getId())
                .type(messageHistoryModel.getType())
                .body(messageHistoryModel.getBody())
                .to(messageHistoryModel.getTo())
                .from(messageHistoryModel.getFrom())
                .created(messageHistoryModel.getCreated().toInstant().toEpochMilli())
                .seen(messageHistoryModel.getSeen())
                .build();
    }

    private Collection<MessageHistory> mapMessageHistories(Collection<MessageHistoryModel> model) {
        return new ArrayList<>(model
                .stream().map(this::mapMessageHistory).toList());
    }

    private PagedData<MessageHistory> mapPagedData(GenericPagedModel<MessageHistoryModel> model) {
        return PagedData.<MessageHistory>builder()
                .totalElements(model.getTotalElements())
                .numberOfElements(model.getNumberOfElements())
                .totalPages(model.getTotalPages())
                .content(mapMessageHistories(model.getContent()))
                .build();
    }
}
