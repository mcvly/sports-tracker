package org.mcvly.tracker.model.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mcvly.tracker.core.Activity;
import org.mcvly.tracker.core.Exercise;
import org.mcvly.tracker.core.ExerciseSet;
import org.mcvly.tracker.core.Person;
import org.mcvly.tracker.core.PersonStats;
import org.mcvly.tracker.core.Training;
import org.mcvly.tracker.core.TrainingSubType;
import org.mcvly.tracker.core.TrainingType;
import org.mcvly.tracker.model.config.PersistenceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author mcvly
 * @since 31.03.14
 */
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SportTrackerServiceTest.Application.class)
@DbUnitConfiguration(dataSetLoader = CustomDataSetLoader.class)
@DatabaseSetup("classpath:/sql/")
public class SportTrackerServiceTest {

    @Configuration
    @EnableAutoConfiguration
    @PropertySource("classpath:application.properties")
    @ComponentScan("org.mcvly.tracker.model") // discover service bean
    @Import(PersistenceConfig.class) // persistence settings
    static class Application {

        public static void main(String[] args) {
            SpringApplication app = new SpringApplication(Application.class);
            app.setShowBanner(false);
            app.run(args);
        }

    }

    @Resource
    private SportTrackerService sportTrackerService;

    @Test
    public void testReadTrainingTypes() {
        assertEquals(4, sportTrackerService.getTrainingTypes().size());
    }

    @Test
    public void testReadTrainingSubTypes() {
        assertEquals(8, sportTrackerService.getTrainingSubtypes(1).size());
    }

    @Test
    public void testPersonInformation() throws STServiceException {
        Person person = sportTrackerService.getPersonInformation(101);
        assertNotNull(person);
        assertEquals("mcvly", person.getName());
        assertEquals(new Integer(173), person.getHeight());
        assertEquals(LocalDate.of(1990, 3, 10), person.getBirth());
        assertTrue(person.getStats().isEmpty());
    }

    @Test(expected = STServiceException.class)
    public void testStatsOfNonExistedPerson() throws STServiceException {
        sportTrackerService.getPersonStats(102, 1);
    }


    @Test
    public void testPersonStats() throws STServiceException {
        List<PersonStats> stats = sportTrackerService.getPersonStats(101, 10);
        assertEquals(2, stats.size());
        stats = sportTrackerService.getPersonStats(101, 1);
        assertEquals(1, stats.size());

        PersonStats stat2 = new PersonStats();
        stat2.setMeasureDate(LocalDateTime.of(2014, 4, 11, 11, 11, 11));
        stat2.setWeight(67.0);

        assertEquals(stat2, stats.get(0));
    }

    @Test
    public void testTrainingInfo() {
        List<Training> trainings = sportTrackerService.getTrainingInfos(101, LocalDateTime.of(2014, 3, 2, 0, 0, 0));
        assertEquals(2, trainings.size());
        trainings = sportTrackerService.getTrainingInfos(101, LocalDateTime.of(2014, 3, 31, 0, 0, 0));
        assertEquals(1, trainings.size());
    }

    @Test
    public void testTrainingList() {
        List<Training> trainings = sportTrackerService.getTrainingsWithExercises(101, 0, 10);
        assertEquals(2, trainings.size());
        trainings = sportTrackerService.getTrainingsWithExercises(101, 0, 1);
        assertEquals(1, trainings.size());
    }

    @Test
    public void testTrainingInsert() throws STServiceException {
        assertEquals(2, sportTrackerService.getTrainingsWithExercises(101, 0, 10).size());
        Training my = new Training();
        my.setType(sportTrackerService.getTrainingTypes().get(0));
        my.setTrainingStart(LocalDateTime.now());
        my.setTrainingStop(LocalDateTime.now().plusHours(1));
        sportTrackerService.addTraining(101, my);
        assertEquals(3, sportTrackerService.getTrainingsWithExercises(101, 0, 10).size());
    }

    @Test
    public void testTrainingUpdate() throws STServiceException {
        List<Training> trainings = sportTrackerService.getTrainingsWithExercises(101, 0, 1);
        assertEquals(1, trainings.size());
        Training trainingToUpdate = trainings.get(0);
        assertEquals(1, trainingToUpdate.getExercises().size());

        Exercise exercise = createExercise(null);
        sportTrackerService.addTrainingExercise(trainingToUpdate.getId(), exercise);

        trainings = sportTrackerService.getTrainingsWithExercises(101, 0, 1);
        assertEquals(1, trainings.size());
        trainingToUpdate = trainings.get(0);
        assertEquals(2, trainingToUpdate.getExercises().size());
        assertEquals(exercise, trainingToUpdate.getExercises().get(1));

        sportTrackerService.addTrainingExercises(trainingToUpdate.getId(),
                Arrays.asList(createExercise(null), createExercise(null)));
        trainings = sportTrackerService.getTrainingsWithExercises(101, 0, 1);
        assertEquals(1, trainings.size());
        trainingToUpdate = trainings.get(0);
        assertEquals(4, trainingToUpdate.getExercises().size());
    }

    @Test
    public void testAddPersonStat() throws STServiceException {
        assertEquals(2, sportTrackerService.getPersonStats(101, 10).size());
        PersonStats stat1 = new PersonStats();
        stat1.setMeasureDate(LocalDateTime.now());
        stat1.setWeight(64.8);
        sportTrackerService.addStat(101, stat1);
        assertEquals(3, sportTrackerService.getPersonStats(101, 10).size());
    }

    @Test
    public void testAddActivity() {
        List<Activity> activities = sportTrackerService.getActivities();
        assertEquals(40, activities.size());

        Activity activity = new Activity();
        TrainingType type = sportTrackerService.getTrainingTypes().get(0);
        TrainingSubType subType = sportTrackerService.getTrainingSubtypes(type.getId()).get(0);
        String name = "activityTest";
        String desc = "activityDesc";

        activity.setType(type);
        activity.setSubType(subType);
        activity.setName(name);
        activity.setDescription(desc);

        sportTrackerService.addActivity(activity);

        activities = sportTrackerService.getActivities();
        assertEquals(41, activities.size());
        Activity persisted = activities.get(40);
        assertNotNull(persisted);
        assertEquals(name, persisted.getName());
        assertEquals(desc, persisted.getDescription());
        assertEquals(type, persisted.getType());
        assertEquals(subType, persisted.getSubType());
    }

    @Test
    public void testUpdateActivity() throws STServiceException {
        List<Activity> activities = sportTrackerService.getActivities();
        assertEquals(40, activities.size());

        TrainingType type = sportTrackerService.getTrainingTypes().get(0);
        TrainingSubType subType = sportTrackerService.getTrainingSubtypes(type.getId()).get(0);
        String name = "activityTest";
        String desc = "activityDesc";

        Activity activityToUpdate = activities.get(0);
        Activity activity = new Activity();
        activity.setId(activityToUpdate.getId());
        activity.setName(name);
        activity.setDescription(desc);
        activity.setType(type);
        activity.setSubType(subType);

        sportTrackerService.updateActivity(activity);

        activities = sportTrackerService.getActivities();
        assertEquals(40, activities.size());
        Activity persisted = activities.get(0);
        assertEquals(name, persisted.getName());
        assertEquals(desc, persisted.getDescription());
        assertEquals(type, persisted.getType());
        assertEquals(subType, persisted.getSubType());
    }

    private Exercise createExercise(Training training) {
        Exercise exercise = new Exercise();
        exercise.setTraining(training);
        exercise.setActivity(sportTrackerService.getActivities().get(0));

        ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setReps(6);
        exerciseSet.setResult(50.0);
        exerciseSet.setNote("note2");

        ExerciseSet exerciseSet2 = new ExerciseSet();
        exerciseSet2.setReps(5);
        exerciseSet2.setResult(50.0);
        exerciseSet2.setNote("note3");

        List<ExerciseSet> exerciseSets = new ArrayList<>();
        exerciseSets.add(exerciseSet);
        exerciseSets.add(exerciseSet2);
        exercise.setExerciseSets(exerciseSets);

        return exercise;
    }
}
