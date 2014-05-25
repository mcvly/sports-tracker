package org.mcvly.tracker.repository;

import org.mcvly.tracker.core.TrainingSubType;
import org.mcvly.tracker.core.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mcvly
 * @since 31.03.14
 */
@Repository
public interface TrainingSubTypeRepository extends JpaRepository<TrainingSubType, Integer> {

    public List<TrainingSubType> findByType(@Param("type")TrainingType type);

}
