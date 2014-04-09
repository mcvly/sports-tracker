package org.mcvly.tracker.model.service.impl;

import org.mcvly.tracker.core.Activity;
import org.mcvly.tracker.core.Exercise;
import org.mcvly.tracker.core.Person;
import org.mcvly.tracker.core.PersonStats;
import org.mcvly.tracker.core.Training;
import org.mcvly.tracker.core.TrainingSubType;
import org.mcvly.tracker.core.TrainingType;
import org.mcvly.tracker.model.repository.ActivityRepository;
import org.mcvly.tracker.model.repository.PersonRepository;
import org.mcvly.tracker.model.repository.TrainingRepository;
import org.mcvly.tracker.model.repository.TrainingSubTypeRepository;
import org.mcvly.tracker.model.repository.TrainingTypeRepository;
import org.mcvly.tracker.model.service.STServiceException;
import org.mcvly.tracker.model.service.SportTrackerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mcvly
 * @since 31.03.14
 */
@Service
public class SportTrackerServiceImpl implements SportTrackerService {

    @Resource
    private PersonRepository personRepository;

    @Resource
    private TrainingTypeRepository trainingTypeRepository;

    @Resource
    private TrainingSubTypeRepository trainingSubTypeRepository;

    @Resource
    private TrainingRepository trainingRepository;

    @Resource
    private ActivityRepository activityRepository;

    @Override
    public Person getPersonInformation(Integer personId) {
        return personRepository.getInfo(personId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PersonStats> getPersonStats(Integer personId, Integer size) throws STServiceException {
        Person p = personRepository.findOne(personId);
        if (p == null) {
            throw new STServiceException("Person with id=" + personId + " not found");
        }
        List<PersonStats> stats = new ArrayList<>(p.getStats());
        stats.sort((o1, o2) -> o2.getMeasureDate().compareTo(o1.getMeasureDate()));
        return stats.subList(0, size < stats.size() ? size : stats.size());

    }

    @Transactional(readOnly = true)
    @Override
    public List<Training> getTrainingInfos(Integer personId, LocalDateTime since) {
        Person p = new Person();
        p.setId(personId);
        return trainingRepository.findByTraineeAndTrainingStartAfter(p, since);
    }

    @Override
    public List<Training> getTrainingsWithExercises(Integer personId, Integer page, Integer size) {
        Person trainee = new Person();
        trainee.setId(personId);
        PageRequest pageRequest = new PageRequest(page, size);
        return trainingRepository.findByTraineeOrderByTrainingStartDesc(trainee, pageRequest);
    }

    @Override
    public List<TrainingType> getTrainingTypes() {
        return trainingTypeRepository.findAll();
    }

    @Override
    public List<TrainingSubType> getTrainingSubtypes(Integer typeId) {
        return trainingSubTypeRepository.findAll();
    }

    @Transactional
    @Override
    public void addTraining(Integer personId, Training training) throws STServiceException {
        Person trainee = personRepository.findOne(personId);
        if (trainee == null) {
            throw new STServiceException("Person with id=" + personId + " not found");
        }
        trainee.addTraining(training);
        training.setTrainee(trainee);
        trainingRepository.save(training);
    }

    @Transactional
    @Override
    public void addTrainingExercise(Long trainingId, Exercise exercise) throws STServiceException {
        Training training = trainingRepository.findOne(trainingId);
        if (training == null) {
            throw new STServiceException("Training with id=" + trainingId + " not found");
        }
        training.addExercise(exercise);
        trainingRepository.save(training);
    }

    @Transactional
    @Override
    public void addTrainingExercises(Long trainingId, List<Exercise> exercises) {
        Training training = trainingRepository.findOne(trainingId);
        exercises.forEach(training::addExercise);
        trainingRepository.save(training);
    }

    @Transactional
    @Override
    public void addStat(Integer personId, PersonStats stat) throws STServiceException {
        Person p = personRepository.findOne(personId);
        if (p == null) {
            throw new STServiceException("Person with id=" + personId + " not found");
        }
        p.addStats(stat);
        personRepository.save(p);
    }

    @Override
    public List<Activity> getActivities() {
        return activityRepository.findAll();
    }

    @Override
    public void addActivity(Activity activity) {
        activityRepository.save(activity);
    }

    @Override
    public void updateActivity(Activity activity) throws STServiceException {
        if (activity.getId() == null) {
            throw new STServiceException("Activity must have valid id");
        }
        activityRepository.save(activity);
    }
}
