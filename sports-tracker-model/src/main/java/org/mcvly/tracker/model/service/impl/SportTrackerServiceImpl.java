package org.mcvly.tracker.model.service.impl;

import org.mcvly.tracker.core.Person;
import org.mcvly.tracker.core.PersonStats;
import org.mcvly.tracker.core.Training;
import org.mcvly.tracker.core.TrainingSubType;
import org.mcvly.tracker.core.TrainingType;
import org.mcvly.tracker.model.repository.PersonRepository;
import org.mcvly.tracker.model.repository.TrainingRepository;
import org.mcvly.tracker.model.repository.TrainingSubTypeRepository;
import org.mcvly.tracker.model.repository.TrainingTypeRepository;
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

    @Override
    public Person getPersonInformation(Integer personId) {
        Person p = personRepository.findOne(personId);
        Person p1 = new Person();
        p1.setBirth(p.getBirth());
        p1.setName(p.getName());
        p1.setHeight(p.getHeight());

        return p1;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PersonStats> getPersonStats(Integer personId, Integer size) {
        Person p = personRepository.findOne(personId);
        List<PersonStats> stats = new ArrayList<>(p.getStats());
        stats.sort((o1, o2) -> o2.getMeasureDate().compareTo(o1.getMeasureDate()));
        return stats.subList(0, size < stats.size() ? size : stats.size());

    }

    @Transactional(readOnly = true)
    @Override
    public List<Training> getTrainingInfos(Integer personId, LocalDateTime since) {
        Person p = new Person();
        p.setId(personId);
        List<Training> trainings = trainingRepository.findByTraineeAndTrainingStartAfter(p, since);
        List<Training> resultTrainings = new ArrayList<>(trainings.size());
        for (Training training : trainings) {
            Training to = new Training();
            to.setTrainingStart(training.getTrainingStart());
            to.setTrainingStop(training.getTrainingStop());
            to.setType(training.getType());
            resultTrainings.add(to);
        }

        return resultTrainings;
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
}
