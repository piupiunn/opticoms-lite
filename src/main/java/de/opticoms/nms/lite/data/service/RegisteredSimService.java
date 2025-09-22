package de.opticoms.nms.lite.data.service;

import de.opticoms.nms.lite.data.model.RegisteredSimModel;
import de.opticoms.nms.lite.data.repository.RegisteredSimRepository;
import de.opticoms.nms.lite.data.util.GenericPagedModel;
import de.opticoms.nms.lite.util.SortDirection;
import de.opticoms.nms.lite.util.UserRole;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.web.client.HttpClientObservationsAutoConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Objects;

@Slf4j
@Service
public class RegisteredSimService {
    final RegisteredSimRepository registeredSimRepository;

    @Autowired
    public RegisteredSimService(RegisteredSimRepository registeredSimRepository) {
        this.registeredSimRepository = registeredSimRepository;
    }

    public RegisteredSimModel findById(Integer id) {
        return getRegisteredSim(id);
    }

    public RegisteredSimModel findByImsi(String imsi) {
        try {
            if (imsi.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imsi cannot be empty");
            }
            val found = registeredSimRepository.findByImsi(imsi);
            if (Objects.isNull(found)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sim with given imsi not found");
            }
            return found;
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

public RegisteredSimModel findByUsername(String username) {
    try {
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be empty");
        }
        String u = username.trim();
        if (u.endsWith(":")) u = u.substring(0, u.length() - 1).trim();
        u = u.toLowerCase();

        if (u.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be empty");
        }
        val found = registeredSimRepository.findByUsername(u);
        if (Objects.isNull(found)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sim with given username not found");
        }
        return found;
    } catch (final DataIntegrityViolationException e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
    }
}


    public GenericPagedModel<RegisteredSimModel> findAll
            (int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? registeredSimRepository.findAll
                    (PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : registeredSimRepository.findAll
                    (PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found");
            }
            return GenericPagedModel.<RegisteredSimModel>builder()
                    .content(result.getContent())
                    .totalPages(result.getTotalPages())
                    .numberOfElements(result.getNumberOfElements())
                    .totalElements(result.getTotalElements())
                    .build();
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public GenericPagedModel<RegisteredSimModel> findAllByRole
            (String role, int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? registeredSimRepository.findAllByRole
                    (UserRole.of(role).name(), PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : registeredSimRepository.findAllByRole
                    (UserRole.of(role).name(), PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with role: ".concat(role));
            }
            return GenericPagedModel.<RegisteredSimModel>builder()
                    .content(result.getContent())
                    .totalPages(result.getTotalPages())
                    .numberOfElements(result.getNumberOfElements())
                    .totalElements(result.getTotalElements())
                    .build();
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public Boolean existsByImsi(String imsi) {
        try {
            if (imsi.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imsi cannot be empty");
            }
            return registeredSimRepository.existsByImsi(imsi);
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public RegisteredSimModel findByMsisdn(String msisdn) {
        try {
            if (msisdn.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Msisdn cannot be empty");
            }
            return registeredSimRepository.findByMsisdn(msisdn);
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

// RegisteredSimService.java
public Boolean existsByUsername(String username) {
    try {
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be empty");
        }
        // normalize
        String u = username.trim();
        if (u.endsWith(":")) u = u.substring(0, u.length() - 1).trim();
        u = u.toLowerCase(); // DB'ye lowercase yazıyorsun, case farkını kapat

        if (u.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be empty");
        }
        return registeredSimRepository.existsByUsername(u);
    } catch (final DataIntegrityViolationException e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
    }
}


    public RegisteredSimModel save(RegisteredSimModel registeredSimModel) {
        try {
            /*
                TODO:
                    ADD VALIDATOR ?
             */
            return registeredSimRepository.save(registeredSimModel);
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public RegisteredSimModel hardDelete(Integer id) {
        try {
            val registeredSimToHardDelete = getRegisteredSim(id);

            registeredSimRepository.delete(registeredSimToHardDelete);

            return registeredSimToHardDelete;
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public void hardDeleteAll() {
        try {
            registeredSimRepository.deleteAll();

        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    private RegisteredSimModel getRegisteredSim(Integer id) {
        try {
            if (Objects.isNull(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "registeredSim id must not be null");
            }
            val result = registeredSimRepository.findById(id);
            if (Objects.isNull(result)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "registeredSimId:".concat(id.toString()));
            }
            return result;
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }
}
