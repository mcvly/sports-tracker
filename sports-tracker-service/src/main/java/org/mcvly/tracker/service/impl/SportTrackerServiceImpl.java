package org.mcvly.tracker.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.mcvly.tracker.core.Activity;
import org.mcvly.tracker.core.Exercise;
import org.mcvly.tracker.core.Person;
import org.mcvly.tracker.core.PersonStats;
import org.mcvly.tracker.core.Training;
import org.mcvly.tracker.core.TrainingSubType;
import org.mcvly.tracker.core.TrainingType;
import org.mcvly.tracker.repository.ActivityRepository;
import org.mcvly.tracker.repository.PersonRepository;
import org.mcvly.tracker.repository.TrainingRepository;
import org.mcvly.tracker.repository.TrainingSubTypeRepository;
import org.mcvly.tracker.repository.TrainingTypeRepository;
import org.mcvly.tracker.service.STServiceException;
import org.mcvly.tracker.service.SportTrackerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<PersonStats> getPersonStats(Integer personId) throws STServiceException {
        Person p = personRepository.findOne(personId);
        if (p == null) {
            throw new STServiceException("Person with id=" + personId + " not found");
        }
        List<PersonStats> stats = new ArrayList<>(p.getStats());
        stats.sort((o1, o2) -> o2.getMeasureDate().compareTo(o1.getMeasureDate()));
//        return stats.subList(0, size < stats.size() ? size : stats.size());
        return stats;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Training> getTrainingInfos(Integer personId, LocalDate since) {
        Person p = new Person();
        p.setId(personId);
        if (since == null) {
            return trainingRepository.findByTrainee(p);
        } else {
            return trainingRepository.findByTraineeAndTrainingStartAfter(p, since.atStartOfDay());
        }
    }

    @Override
    public List<Training> getTrainingsWithExercises(Integer personId, Integer page, Integer size) {
        Person trainee = new Person();
        trainee.setId(personId);
        PageRequest pageRequest = new PageRequest(page, size);
        return trainingRepository.findByTraineeOrderByTrainingStartDesc(trainee, pageRequest);
    }

    @Override
    public Training getTraining(Long trainingId) throws STServiceException {
        Training training = trainingRepository.findOne(trainingId);
        if (training == null) {
            throw new STServiceException("Training with id=" + trainingId + " not found");
        }

        return training;
    }

    @Override
    public List<TrainingType> getTrainingTypes() {
        return trainingTypeRepository.findAll();
    }

    @Override
    public List<TrainingSubType> getTrainingSubtypes(Integer typeId) throws STServiceException {
        TrainingType t = trainingTypeRepository.findOne(typeId);
        if (t == null) {
            throw new STServiceException("Can't find training type with id=" + typeId);
        } else {
            return trainingSubTypeRepository.findByType(t);
        }
    }

    @Transactional
    @Override
    public Training addTraining(Integer personId, Training training) throws STServiceException {
        Person trainee = personRepository.findOne(personId);
        if (trainee == null) {
            throw new STServiceException("Person with id=" + personId + " not found");
        }
        trainee.addTraining(training);
        training.setTrainee(trainee);
        return trainingRepository.save(training);
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
    public Activity addActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    @Override
    public void updateActivity(Activity activity) throws STServiceException {
        if (activity.getId() == null) {
            throw new STServiceException("Activity must have valid id");
        }
        activityRepository.save(activity);
    }
}
