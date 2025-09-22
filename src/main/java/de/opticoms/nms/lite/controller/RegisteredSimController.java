package de.opticoms.nms.lite.controller;

import de.opticoms.nms.lite.controller.model.PagedData;
import de.opticoms.nms.lite.controller.model.RegisteredSim;
import de.opticoms.nms.lite.controller.model.UpdateTcAndAddress;
import de.opticoms.nms.lite.data.model.RegisteredSimModel;
import de.opticoms.nms.lite.data.service.RegisteredSimService;
import de.opticoms.nms.lite.data.util.GenericPagedModel;
import de.opticoms.nms.lite.util.SortDirection;
import de.opticoms.nms.lite.util.UserRole;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

import static de.opticoms.nms.lite.data.util.Parsers.tryParseInteger;

@Slf4j
@RestController
public class RegisteredSimController {
    final RegisteredSimService registeredSimService;

    @Autowired
    public RegisteredSimController(RegisteredSimService registeredSimService) {
        this.registeredSimService = registeredSimService;
    }

    @RequestMapping(value = "/v1/registeredSim/find_by_id", method = RequestMethod.GET)
    private ResponseEntity<RegisteredSim> findUser(@RequestParam String id) {
        log.info("Calling: findUserById ".concat(id));

        val result = registeredSimService.findById(tryParseInteger(id, "id"));

        return ResponseEntity.ok(mapRegisteredSim(result));
    }

    @RequestMapping(value = "/v1/registeredSim/find_by_username", method = RequestMethod.GET)
    private ResponseEntity<RegisteredSim> findUserByUsername(@RequestParam String username) {
        log.info("Calling: findUserByUsername ".concat(username));

        val result = registeredSimService.findByUsername(username);

        return ResponseEntity.ok(mapRegisteredSim(result));
    }

    @RequestMapping(value = "/v1/registeredSim/update_tc_and_address", method = RequestMethod.POST)
    private ResponseEntity<RegisteredSim> updateTcOrAddress(@RequestBody UpdateTcAndAddress updatedSim) {
        log.info("Calling: updateTcOrAddress >> user: ".concat(updatedSim.getId().toString()));

        if (Objects.isNull(updatedSim.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id cannot be null");
        }

        val sim = registeredSimService.findById(updatedSim.getId());

        sim.setTc(updatedSim.getTc());
        sim.setAddress(updatedSim.getAddress());


        val saved = registeredSimService.save(sim);
        return ResponseEntity.ok(mapRegisteredSim(saved));
    }

    @RequestMapping(value = "/v1/registeredSim/find_all", method = RequestMethod.GET)
    private ResponseEntity<PagedData<RegisteredSim>> findAllUsers
            (@RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("Calling: findAllUsers >> ");

        val response = registeredSimService.findAll(pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/registeredSim/find_all_by_role/{role}", method = RequestMethod.GET)
    private ResponseEntity<PagedData<RegisteredSim>> findUsersByRole
            (@PathVariable String role,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("Calling: findUsersByRole >> ".concat(role));

        val response = registeredSimService.findAllByRole(role, pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(response));
    }

    @RequestMapping(value = "/v1/registeredSim/find_by_msisdn/{msisdn}", method = RequestMethod.GET)
    private ResponseEntity<RegisteredSim> findUserByMsisdn(@PathVariable String msisdn) {
        log.info("Calling: findUserByMsisdn >> ".concat(msisdn));

        val response = registeredSimService.findByMsisdn(msisdn);

        return ResponseEntity.ok(mapRegisteredSim(response));
    }

    @RequestMapping(value = "/v1/registeredSim/find_by_imsi/{imsi}", method = RequestMethod.GET)
    private ResponseEntity<RegisteredSim> findUserByImsi(@PathVariable String imsi) {
        log.info("Calling: findUserByImsi >> ".concat(imsi));

        val response = registeredSimService.findByImsi(imsi);

        return ResponseEntity.ok(mapRegisteredSim(response));
    }

    @RequestMapping(value = "/v1/registeredSim/save", method = RequestMethod.POST)
    private ResponseEntity<RegisteredSim> saveNewRegisteredSim(@RequestBody RegisteredSim registeredSim) {
        log.info("Calling: saveNewRegisteredSim >> ".concat(registeredSim.getImsi()));

        val exists = registeredSimService.existsByImsi(registeredSim.getImsi());

        if (!exists) {
            val model = RegisteredSimModel.builder()
                    .imsi(registeredSim.getImsi())
                    .msisdn(registeredSim.getMsisdn())
                    .tc(registeredSim.getTc())
                    .address(registeredSim.getAddress())
                    .fullname(registeredSim.getFullname())
                    .role(UserRole.of(registeredSim.getRole()).name())
                    .username(mapUserName(registeredSim.getFullname()))
                    .created(new Date(Instant.now().toEpochMilli()))
                    .deactivated(null)
                    .build();

            val saved = registeredSimService.save(model);

            return ResponseEntity.ok(mapRegisteredSim(saved));
        } else {
            val found = registeredSimService.findByImsi(registeredSim.getImsi());
            if (Objects.nonNull(found.getDeactivated())) {
                found.setCreated(new Date(Instant.now().toEpochMilli()));
                found.setDeactivated(null);
                val saved = registeredSimService.save(found);
                return ResponseEntity.ok(mapRegisteredSim(saved));
            } else {
                return ResponseEntity.ok(mapRegisteredSim(found));
            }
        }
    }

    private Collection<RegisteredSim> mapRegisteredSims(Collection<RegisteredSimModel> registeredSimModels) {
        return new ArrayList<>(registeredSimModels
                .stream()
                .map(this::mapRegisteredSim).toList());
    }

    private RegisteredSim mapRegisteredSim(RegisteredSimModel registeredSim) {
        return RegisteredSim.builder()
                .id(registeredSim.getId())
                .imsi(registeredSim.getImsi())
                .tc(registeredSim.getTc())
                .address(registeredSim.getAddress())
                .msisdn(registeredSim.getMsisdn())
                .fullname(registeredSim.getFullname())
                .role(registeredSim.getRole())
                .username(registeredSim.getUsername())
                .created(Objects.nonNull(registeredSim.getCreated())
                        ? registeredSim.getCreated().toInstant().toEpochMilli()
                        : null)
                .deactivated(Objects.nonNull(registeredSim.getDeactivated())
                        ? registeredSim.getDeactivated().toInstant().toEpochMilli()
                        : null)
                .build();
    }

    private String mapUserName(String username) {
        try {
            val letters = Map.of(
                    'ü', 'u',
                    'ö', 'o',
                    'ı', 'i',
                    'ş', 's',
                    'ğ', 'g',
                    'ç', 'c');

            val mapped = new StringBuilder();
            for (val c : username.toLowerCase().toCharArray()) {
                if (letters.containsKey(c)) {
                    mapped.append(letters.get(c));
                } else {
                    mapped.append(c);
                }
            }

            return mapped.toString().replaceAll("\\s+", "-")
                    .concat("-").concat(UUID.randomUUID().toString());
        } catch (Exception e) {
            throw new RuntimeException("Error occur at username mapping", e);
        }
    }

    private PagedData<RegisteredSim> mapPagedData(GenericPagedModel<RegisteredSimModel> model) {
        return PagedData.<RegisteredSim>builder()
                .totalElements(model.getTotalElements())
                .numberOfElements(model.getNumberOfElements())
                .totalPages(model.getTotalPages())
                .content(mapRegisteredSims(model.getContent()))
                .build();
    }
}
