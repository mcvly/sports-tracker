package org.mcvly.tracker.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mcvly.tracker.core.Person;
import org.mcvly.tracker.core.PersonStats;
import org.mcvly.tracker.core.Training;
import org.mcvly.tracker.model.service.SportTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@ContextConfiguration(classes = {PersistenceContext.class})
@DbUnitConfiguration(dataSetLoader = CustomDataSetLoader.class)
@DatabaseSetup("classpath:/sql")
public class SportTrackerServiceTest {

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
    public void testPersonInformation() {
        Person person = sportTrackerService.getPersonInformation(101);
        assertNotNull(person);
        assertEquals("mcvly", person.getName());
        assertEquals(new Integer(173), person.getHeight());
        assertEquals(LocalDate.of(1990, 3, 10), person.getBirth());
        assertTrue(person.getStats().isEmpty());
    }

    @Test
    public void testPersonStats() {
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
    public void testInsertTraining() {
        Training my = new Training();
        my.setType(sportTrackerService.getTrainingTypes().get(0));
        my.setTrainingStart(LocalDateTime.now());
        my.setTrainingStop(LocalDateTime.now().plusHours(1));
        sportTrackerService.addTraining(101, my);
        System.out.println(sportTrackerService.getTrainingsWithExercises(101, 0, 10));
        sportTrackerService.getPersonInformation(101);
    }
}
