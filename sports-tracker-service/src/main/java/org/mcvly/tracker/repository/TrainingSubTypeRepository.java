package org.mcvly.tracker.repository;

import org.mcvly.tracker.core.TrainingSubType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mcvly
 * @since 31.03.14
 */
@Repository
public interface TrainingSubTypeRepository extends JpaRepository<TrainingSubType, Integer> {
}
