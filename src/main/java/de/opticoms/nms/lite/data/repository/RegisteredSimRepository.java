package de.opticoms.nms.lite.data.repository;

import de.opticoms.nms.lite.data.model.RegisteredSimModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredSimRepository extends PagingAndSortingRepository<RegisteredSimModel, Integer> {
    RegisteredSimModel findByImsi(String imsi);

    Boolean existsByImsi(String imsi);

    Boolean existsByUsername(String username);

    Page<RegisteredSimModel> findAllByRole(String role, Pageable pageable);

    void delete(RegisteredSimModel model);

    void deleteAll();

    RegisteredSimModel save(RegisteredSimModel model);

    RegisteredSimModel findById(Integer id);
    RegisteredSimModel findByMsisdn(String msisdn);
    RegisteredSimModel findByUsername(String username);
}
