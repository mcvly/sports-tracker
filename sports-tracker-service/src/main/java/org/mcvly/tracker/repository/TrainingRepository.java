package org.mcvly.tracker.repository;

import org.mcvly.tracker.core.Person;
import org.mcvly.tracker.core.Training;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author mcvly
 * @since 03.04.14
 */
public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("select new Training (t.type, t.trainingStart, t.trainingStop) from Training t where t.trainee = :trainee and t.trainingStart > :startDate")
    public List<Training> findByTraineeAndTrainingStartAfter(@Param("trainee") Person trainee,
                                                             @Param("startDate") LocalDateTime startDate);

    public List<Training> findByTrainee(@Param("trainee") Person trainee);

    @Query("from Training t left join fetch t.exercises e where t.trainee = :trainee order by t.trainingStart desc")
    public List<Training> findByTraineeOrderByTrainingStartDesc(@Param("trainee") Person trainee, Pageable page);

}
