package org.mcvly.tracker.repository;

import org.mcvly.tracker.core.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mcvly
 * @since 31.03.14
 */
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {
}
