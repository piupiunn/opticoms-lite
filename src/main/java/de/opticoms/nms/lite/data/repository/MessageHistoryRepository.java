package de.opticoms.nms.lite.data.repository;

import de.opticoms.nms.lite.data.model.LocationModel;
import de.opticoms.nms.lite.data.model.MessageHistoryModel;
import de.opticoms.nms.lite.data.model.RegisteredSimModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface MessageHistoryRepository extends PagingAndSortingRepository<MessageHistoryModel, Integer> {
    MessageHistoryModel findById(int id);

    void delete(MessageHistoryModel messageHistoryModel);

    void deleteAll();

    MessageHistoryModel save(MessageHistoryModel messageHistoryModel);

    Page<MessageHistoryModel> findAllByFromAndToAndSeenAndCreatedBeforeAndCreatedAfter
            (String from, String to, Boolean seen, Date createdBefore, Date createdAfter, Pageable pageable);
    Page<MessageHistoryModel> findAllByToAndSeenAndCreatedBeforeAndCreatedAfter
            (String to, Boolean seen, Date createdBefore, Date createdAfter, Pageable pageable);
    Page<MessageHistoryModel> findAllByFromAndSeenAndCreatedBeforeAndCreatedAfter
            (String from, Boolean seen, Date createdBefore, Date createdAfter, Pageable pageable);
    Page<MessageHistoryModel> findAllByFromAndToAndCreatedBeforeAndCreatedAfter
            (String from, String to, Date createdBefore, Date createdAfter, Pageable pageable);
    Page<MessageHistoryModel> findAllByToAndCreatedBeforeAndCreatedAfter
            (String to, Date createdBefore, Date createdAfter, Pageable pageable);
    Page<MessageHistoryModel> findAllByFromAndCreatedBeforeAndCreatedAfter
            (String fro, Date createdBefore, Date createdAfter, Pageable pageable);
}
