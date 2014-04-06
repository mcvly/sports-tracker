package org.mcvly.tracker.model.service;

import org.mcvly.tracker.core.Person;
import org.mcvly.tracker.core.PersonStats;
import org.mcvly.tracker.core.Training;
import org.mcvly.tracker.core.TrainingSubType;
import org.mcvly.tracker.core.TrainingType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author mcvly
 * @since 31.03.14
 */
public interface SportTrackerService {

    Person getPersonInformation(Integer personId);

    List<PersonStats> getPersonStats(Integer personId, Integer size);

    List<Training> getTrainingInfos(Integer personId, LocalDateTime since);

    List<Training> getTrainingsWithExercises(Integer personId, Integer page, Integer size);

    List<TrainingType> getTrainingTypes();

    List<TrainingSubType> getTrainingSubtypes(Integer typeId);

}
