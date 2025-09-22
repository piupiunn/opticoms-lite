package de.opticoms.nms.lite.controller;

import de.opticoms.nms.lite.controller.model.Location;
import de.opticoms.nms.lite.controller.model.PagedData;
import de.opticoms.nms.lite.data.model.LocationModel;
import de.opticoms.nms.lite.data.service.LocationService;
import de.opticoms.nms.lite.data.util.GenericPagedModel;
import de.opticoms.nms.lite.util.LocationReason;
import de.opticoms.nms.lite.util.SortDirection;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@RestController
public class LocationController {
    final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @RequestMapping(value = "/v1/location/find_all_by_imsi/{imsi}", method = RequestMethod.GET)
    public ResponseEntity<PagedData<Location>> findAllByImsi
            (@PathVariable String imsi,
             @RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "desc") String sortDir) {

        log.info("Calling: findAllByImsi >> ".concat(imsi));

        val result = locationService.findAllByImsi
                (imsi, pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(result));
    }

    @RequestMapping(value = "/v1/location/find_all", method = RequestMethod.GET)
    public ResponseEntity<PagedData<Location>> findAllByImsi
            (@RequestParam(defaultValue = "0") int pageNo,
             @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "id") String sortBy,
             @RequestParam(defaultValue = "desc") String sortDir) {

        log.info("Calling: findAll >> ");

        val result = locationService.findAll(pageNo, pageSize, sortBy, SortDirection.of(sortDir));

        return ResponseEntity.ok(mapPagedData(result));
    }

    @RequestMapping(value = "/v1/location/save", method = RequestMethod.POST)
    public ResponseEntity<Location> saveLocation(@RequestBody Location location) {
        log.info("Calling: saveLocation >> ".concat(location.toString()));

        val saved = locationService.save(LocationModel.builder()
                        .imsi(location.getImsi())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .altitude(location.getAltitude())
                        .accuracy(location.getAccuracy())
                        .altitudeAccuracy(location.getAltitudeAccuracy())
                        .heading(location.getHeading())
                        .speed(location.getSpeed())
                        .reason(LocationReason.of(location.getReason()).name())
                .build());

        return ResponseEntity.ok(mapLocation(saved));
    }

    private Collection<Location> mapLocations(Collection<LocationModel> locationModels) {
        return new ArrayList<>(locationModels
                .stream()
                .map(this::mapLocation).toList());
    }

    private Location mapLocation(LocationModel location) {
        return Location.builder()
                .id(location.getId())
                .imsi(location.getImsi())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .altitude(location.getAltitude())
                .accuracy(location.getAccuracy())
                .altitudeAccuracy(location.getAltitudeAccuracy())
                .heading(location.getHeading())
                .speed(location.getSpeed())
                .reason(location.getReason())
                .build();
    }

    private PagedData<Location> mapPagedData(GenericPagedModel<LocationModel> model) {
        return PagedData.<Location>builder()
                .totalElements(model.getTotalElements())
                .numberOfElements(model.getNumberOfElements())
                .totalPages(model.getTotalPages())
                .content(mapLocations(model.getContent()))
                .build();
    }
}
