package org.mcvly.tracker.service;

import java.time.LocalDate;
import java.util.List;

import org.mcvly.tracker.core.Activity;
import org.mcvly.tracker.core.Exercise;
import org.mcvly.tracker.core.Person;
import org.mcvly.tracker.core.PersonStats;
import org.mcvly.tracker.core.Training;
import org.mcvly.tracker.core.TrainingSubType;
import org.mcvly.tracker.core.TrainingType;

/**
 * @author mcvly
 * @since 31.03.14
 */
public interface SportTrackerService {

    Person getPersonInformation(Integer personId);

    List<PersonStats> getPersonStats(Integer personId) throws STServiceException;

    List<Training> getTrainingInfos(Integer personId, LocalDate since);

    List<Training> getTrainingsWithExercises(Integer personId, Integer page, Integer size);

    Training getTraining(Long trainingId) throws STServiceException;

    List<TrainingType> getTrainingTypes();

    List<TrainingSubType> getTrainingSubtypes(Integer typeId) throws STServiceException;

    List<Activity> getActivities();

    Training addTraining(Integer personId, Training training) throws STServiceException;

    void addTrainingExercise(Long trainingId, Exercise exercise) throws STServiceException;

    void addTrainingExercises(Long trainingId, List<Exercise> exercise);

    void addStat(Integer personId, PersonStats stat) throws STServiceException;

    Activity addActivity(Activity activity);

    void updateActivity(Activity activity) throws STServiceException;

}
