package de.opticoms.nms.lite.data.service;

import de.opticoms.nms.lite.data.model.LocationModel;
import de.opticoms.nms.lite.data.model.MessageHistoryModel;
import de.opticoms.nms.lite.data.model.RegisteredSimModel;
import de.opticoms.nms.lite.data.repository.MessageHistoryRepository;
import de.opticoms.nms.lite.data.util.GenericPagedModel;
import de.opticoms.nms.lite.util.SortDirection;
import de.opticoms.nms.lite.util.UserRole;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class MessageHistoryService {
    final MessageHistoryRepository messageHistoryRepository;

    @Autowired
    public MessageHistoryService(MessageHistoryRepository messageHistoryRepository) {
        this.messageHistoryRepository = messageHistoryRepository;
    }


    public GenericPagedModel<MessageHistoryModel> findAllByFromAndToAndSeenAndCreatedBeforeAndCreatedAfter
            (String from, String to, Boolean seen, Date createdBefore, Date createdAfter,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? messageHistoryRepository.findAllByFromAndToAndSeenAndCreatedBeforeAndCreatedAfter
                    (from, to, seen, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : messageHistoryRepository.findAllByFromAndToAndSeenAndCreatedBeforeAndCreatedAfter
                    (from, to, seen, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No message found from: ".concat(from).concat(" to: ".concat(to)
                                .concat(" between: ".concat(createdAfter.toString())
                                        .concat(" - ").concat(createdBefore.toString()))));
            }
            return GenericPagedModel.<MessageHistoryModel>builder()
                    .content(result.getContent())
                    .totalPages(result.getTotalPages())
                    .numberOfElements(result.getNumberOfElements())
                    .totalElements(result.getTotalElements())
                    .build();
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public GenericPagedModel<MessageHistoryModel> findAllByToAndSeenAndCreatedBeforeAndCreatedAfter
            (String to, Boolean seen, Date createdBefore, Date createdAfter,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? messageHistoryRepository.findAllByToAndSeenAndCreatedBeforeAndCreatedAfter
                    (to, seen, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : messageHistoryRepository.findAllByToAndSeenAndCreatedBeforeAndCreatedAfter
                    (to, seen, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No message found to: ".concat(to)
                                .concat(" between: ".concat(createdAfter.toString())
                                        .concat(" - ").concat(createdBefore.toString())));
            }
            return GenericPagedModel.<MessageHistoryModel>builder()
                    .content(result.getContent())
                    .totalPages(result.getTotalPages())
                    .numberOfElements(result.getNumberOfElements())
                    .totalElements(result.getTotalElements())
                    .build();
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public GenericPagedModel<MessageHistoryModel> findAllByFromAndSeenAndCreatedBeforeAndCreatedAfter
            (String from, Boolean seen, Date createdBefore, Date createdAfter,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? messageHistoryRepository.findAllByFromAndSeenAndCreatedBeforeAndCreatedAfter
                    (from, seen, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : messageHistoryRepository.findAllByFromAndSeenAndCreatedBeforeAndCreatedAfter
                    (from, seen, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No message found from: ".concat(from)
                                .concat(" between: ".concat(createdAfter.toString())
                                        .concat(" - ").concat(createdBefore.toString())));
            }
            return GenericPagedModel.<MessageHistoryModel>builder()
                    .content(result.getContent())
                    .totalPages(result.getTotalPages())
                    .numberOfElements(result.getNumberOfElements())
                    .totalElements(result.getTotalElements())
                    .build();
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public GenericPagedModel<MessageHistoryModel> findAllByFromAndToAndCreatedBeforeAndCreatedAfter
            (String from, String to, Date createdBefore, Date createdAfter,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? messageHistoryRepository.findAllByFromAndToAndCreatedBeforeAndCreatedAfter
                    (from, to, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : messageHistoryRepository.findAllByFromAndToAndCreatedBeforeAndCreatedAfter
                    (from, to, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No message found from: ".concat(from).concat(" to: ".concat(to)
                                .concat(" between: ".concat(createdAfter.toString())
                                        .concat(" - ").concat(createdBefore.toString()))));
            }
            return GenericPagedModel.<MessageHistoryModel>builder()
                    .content(result.getContent())
                    .totalPages(result.getTotalPages())
                    .numberOfElements(result.getNumberOfElements())
                    .totalElements(result.getTotalElements())
                    .build();
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public GenericPagedModel<MessageHistoryModel> findAllByToAndCreatedBeforeAndCreatedAfter
            (String to, Date createdBefore, Date createdAfter,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? messageHistoryRepository.findAllByToAndCreatedBeforeAndCreatedAfter
                    (to, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : messageHistoryRepository.findAllByToAndCreatedBeforeAndCreatedAfter
                    (to, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No message found to: ".concat(to)
                                .concat(" between: ".concat(createdAfter.toString())
                                        .concat(" - ").concat(createdBefore.toString())));
            }
            return GenericPagedModel.<MessageHistoryModel>builder()
                    .content(result.getContent())
                    .totalPages(result.getTotalPages())
                    .numberOfElements(result.getNumberOfElements())
                    .totalElements(result.getTotalElements())
                    .build();
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public GenericPagedModel<MessageHistoryModel> findAllByFromAndCreatedBeforeAndCreatedAfter
            (String from, Date createdBefore, Date createdAfter,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? messageHistoryRepository.findAllByFromAndCreatedBeforeAndCreatedAfter
                    (from, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : messageHistoryRepository.findAllByFromAndCreatedBeforeAndCreatedAfter
                    (from, createdBefore, createdAfter, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No message found from: ".concat(from)
                                .concat(" between: ".concat(createdAfter.toString())
                                        .concat(" - ").concat(createdBefore.toString())));
            }
            return GenericPagedModel.<MessageHistoryModel>builder()
                    .content(result.getContent())
                    .totalPages(result.getTotalPages())
                    .numberOfElements(result.getNumberOfElements())
                    .totalElements(result.getTotalElements())
                    .build();
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }


    public MessageHistoryModel findById(Integer id) {
        return getMessage(id);
    }

    public MessageHistoryModel save(MessageHistoryModel message) {
        try {
            /*
                TODO:
                    ADD VALIDATOR ?
             */
            return messageHistoryRepository.save(message);
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public MessageHistoryModel hardDelete(Integer id) {
        try {
            val messageToHardDelete = getMessage(id);

            messageHistoryRepository.delete(messageToHardDelete);

            return messageToHardDelete;
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public void hardDeleteAll() {
        try {
            messageHistoryRepository.deleteAll();

        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    private MessageHistoryModel getMessage(Integer id) {
        try {
            if (Objects.isNull(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Message History id must not be null");
            }
            val result = messageHistoryRepository.findById(id);
            if (Objects.isNull(result)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "messageHistoryId: ".concat(id.toString()));
            }
            return result;
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }
}
