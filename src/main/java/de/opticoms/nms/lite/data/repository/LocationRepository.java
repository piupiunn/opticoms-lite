package de.opticoms.nms.lite.data.repository;

import de.opticoms.nms.lite.data.model.LocationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository  extends PagingAndSortingRepository<LocationModel, Integer> {
    LocationModel findById(int id);

    void delete(LocationModel locationModel);

    void deleteAll();

    LocationModel save(LocationModel locationModel);

    Page<LocationModel> findAll(Pageable pageable);

    Page<LocationModel> findAllByImsi(String imsi, Pageable pageable);

    Page<LocationModel> findAllByLatitudeBetweenAndLongitudeBetween
            (Float minLat, Float maxLat, Float minLon, Float maxLon, Pageable pageable);

}
