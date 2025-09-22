package de.opticoms.nms.lite.data.service;

import de.opticoms.nms.lite.data.model.LocationModel;
import de.opticoms.nms.lite.data.repository.LocationRepository;
import de.opticoms.nms.lite.data.util.GenericPagedModel;
import de.opticoms.nms.lite.util.SortDirection;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
@Service
public class LocationService {
    final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LocationModel findById(Integer id) {
        return getLocation(id);
    }

    public GenericPagedModel<LocationModel> findAllByImsi
            (String imsi, int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? locationRepository.findAllByImsi
                    (imsi, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : locationRepository.findAllByImsi
                    (imsi, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No location found with imsi: ".concat(imsi));
            }
            return GenericPagedModel.<LocationModel>builder()
                    .content(result.getContent())
                    .totalPages(result.getTotalPages())
                    .numberOfElements(result.getNumberOfElements())
                    .totalElements(result.getTotalElements())
                    .build();
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public GenericPagedModel<LocationModel> findAllByLatitudeBetweenAndLongitudeBetween
            (Float minLat, Float maxLat, Float minLon, Float maxLon,
             int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? locationRepository.findAllByLatitudeBetweenAndLongitudeBetween
                    (minLat, maxLat, minLon, maxLon, PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : locationRepository.findAllByLatitudeBetweenAndLongitudeBetween
                    (minLat, maxLat, minLon, maxLon, PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No location found within: "
                        .concat("\nLatitudes: ").concat(minLat.toString()).concat(" ").concat(maxLat.toString())
                        .concat("\nLongitudes: ").concat(minLon.toString()).concat(" ").concat(maxLon.toString()));
            }
            return GenericPagedModel.<LocationModel>builder()
                    .content(result.getContent())
                    .totalPages(result.getTotalPages())
                    .numberOfElements(result.getNumberOfElements())
                    .totalElements(result.getTotalElements())
                    .build();
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public GenericPagedModel<LocationModel> findAll
            (int page, int size, String sortBy, SortDirection sortDirection) {
        try {
            val result = sortDirection.equals(SortDirection.Ascending)
                    ? locationRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy).ascending()))
                    : locationRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy).descending()));
            if (result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No location found");
            }
            return GenericPagedModel.<LocationModel>builder()
                    .content(result.getContent())
                    .totalPages(result.getTotalPages())
                    .numberOfElements(result.getNumberOfElements())
                    .totalElements(result.getTotalElements())
                    .build();
        } catch (final DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(e));
        }
    }

    public LocationModel save(LocationModel location) {
        try {
            /*
                TODO:
                    ADD VALIDATOR ?
             */
            return locationRepository.save(location);
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public LocationModel hardDelete(Integer id) {
        try {
            val locationToHardDelete = getLocation(id);

            locationRepository.delete(locationToHardDelete);

            return locationToHardDelete;
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    public void hardDeleteAll() {
        try {
            locationRepository.deleteAll();

        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }

    private LocationModel getLocation(Integer id) {
        try {
            if (Objects.isNull(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "location id must not be null");
            }
            val result = locationRepository.findById(id);
            if (Objects.isNull(result)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "locationId:".concat(id.toString()));
            }
            return result;
        } catch (final DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex));
        }
    }
}
