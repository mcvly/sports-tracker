package org.mcvly.tracker.model.repository;

import org.mcvly.tracker.core.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author mcvly
 * @since 31.03.14
 */
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {
}
