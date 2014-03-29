package org.mcvly.tracker.test;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mcvly.tracker.model.Activity;
import org.mcvly.tracker.model.Exercise;
import org.mcvly.tracker.model.ExerciseSet;
import org.mcvly.tracker.model.Person;
import org.mcvly.tracker.model.PersonStats;
import org.mcvly.tracker.model.Training;
import org.mcvly.tracker.model.TrainingType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="mailto:RMalyona@luxoft.com">Ruslan Malyona</a>
 * @since 11.02.14
 */
public class ModelTest {

    private static final String TEST_UNIT = "test";
    /** The factory that produces entity manager. */
    private static EntityManagerFactory entityManagerFactory;
    /** The entity manager that persists and queries the DB. */
    private static EntityManager entityManager;

    @BeforeClass
    public static void initTestFixture() throws Exception {
        // Get the entity manager for the tests.
        entityManagerFactory = Persistence.createEntityManagerFactory(TEST_UNIT);
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Cleans up the session.
     */
    @AfterClass
    public static void closeTestFixture() {
        entityManager.close();
        entityManagerFactory.close();
    }

    @Before
    public void populateData() {

        try {
            entityManager.unwrap(Session.class).doWork(connection -> {
                try {
                    connection.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                    IDatabaseConnection dbUnitCon = new DatabaseConnection(connection);
                    IDataSet dataSet = getDataSet();
                    DatabaseOperation.CLEAN_INSERT.execute(dbUnitCon, dataSet);
                } catch (URISyntaxException | DatabaseUnitException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private IDataSet getDataSet() throws URISyntaxException, DataSetException {
        return new CompositeDataSet(
                new CsvDataSet(new File(ClassLoader.getSystemResource("sql").toURI())),
                new FlatXmlDataSetBuilder().build(ClassLoader.getSystemResourceAsStream("initDataSet.xml"))
        );
    }

    @Test
    public void testRead() throws ParseException {
        Person p1 = entityManager.find(Person.class, 101);
        //check person
        checkPerson(p1);
        //check stats
        List<PersonStats> stats = p1.getStats();
        assertEquals(2, stats.size());
        assertEquals(createStat1(), stats.get(0));
        assertEquals(createStat2(), stats.get(1));

        //check trainings
        List<Training> trainings = p1.getTrainings();
        assertEquals(2, trainings.size());

        //check training1
        Training training1 = trainings.get(0);
        checkTraining(training1, createTraining1(p1));
        //check exercises

        assertEquals(2, training1.getExercises().size());
        assertEquals(createExercise1(training1), training1.getExercises().get(0));
        assertEquals(createExercise2(training1), training1.getExercises().get(1));

        //check training2
        Training training2 = trainings.get(1);
        checkTraining(training2, createTraining2(p1));

        //check exercises
        assertEquals(1, training2.getExercises().size());
        assertEquals(createExercise3(training2), training2.getExercises().get(0));
    }

    @Test
    public void testCreate() {
        // check person
        Person p = createPerson();
        persistPerson(p);
        Person p1 = getPerson(p.getId());
        checkPerson(p1);
        //check stats
        p1.addStats(createStat1());
        updatePerson(p1);
        p1 = getPerson(p1.getId());
        assertEquals(1, p1.getStats().size());
        assertEquals(createStat1(), p1.getStats().get(0));
        p1.setStats(new ArrayList<>(Arrays.asList(createStat1(), createStat2())));
        updatePerson(p1);
        p1 = getPerson(p1.getId());
        List<PersonStats> stats = p1.getStats();
        assertEquals(2, stats.size());
        assertEquals(createStat1(), stats.get(0));
        assertEquals(createStat2(), stats.get(1));
        //check training
        Training training = createTraining1(p1);
        Exercise exercise1 = createExercise1(training);
        training.setExercises(new ArrayList<>(Arrays.asList(exercise1)));
        persistTraining(training); // persist with exercises
        //check exercise
        Training training1 = entityManager.find(Training.class, training.getId());
        checkTraining(training1, training);
        assertEquals(1, training1.getExercises().size());
        assertEquals(exercise1, training.getExercises().get(0));
        //check exercise
        Exercise exercise2 = createExercise2(training1);
        persistExercise(exercise2); // persist exercise, training should update
        //check exercise
        training1 = entityManager.find(Training.class, training1.getId());
        assertEquals(2, training1.getExercises().size());
        assertEquals(exercise1, training.getExercises().get(0));
        assertEquals(exercise2, training.getExercises().get(1));

        //check training
        Training training2 = createTraining2(p1);
        persistTraining(training2);
        training = entityManager.find(Training.class, training2.getId());
        checkTraining(training2, training);
        //check exercise
        Exercise exercise3 = createExercise3(training);
        training.addExercise(exercise3);
        updateTraining(training); // add exercise to training
        training2 = entityManager.find(Training.class, training.getId());
        assertEquals(1, training2.getExercises().size());
        assertEquals(exercise3, training2.getExercises().get(0));
    }

    private Person createPerson() {
        Person p = new Person();
        p.setBirth(LocalDate.of(1990, 3, 10));
        p.setHeight(173);
        p.setName("mcvly");
        return p;
    }

    private Person getPerson(Integer id) {
        return entityManager.find(Person.class, id);
    }

    private void updatePerson(Person p1) {
        entityManager.getTransaction().begin();
        entityManager.merge(p1);
        entityManager.getTransaction().commit();
    }

    private void persistPerson(Person p) {
        entityManager.getTransaction().begin();
        entityManager.persist(p);
        entityManager.getTransaction().commit();
    }

    private void checkPerson(Person p) {
        assertNotNull(p);
        Person expected = createPerson();
        assertEquals(expected.getName(), p.getName());
        assertEquals(expected.getBirth(), p.getBirth());
        assertEquals(expected.getHeight(), p.getHeight());
    }

    private PersonStats createStat1() {
        PersonStats stat1 = new PersonStats();
        stat1.setMeasureDate(LocalDateTime.of(2014, 3, 28, 19, 29, 14));
        stat1.setWeight(66.0);

        return stat1;
    }

    private PersonStats createStat2() {
        PersonStats stat2 = new PersonStats();
        stat2.setMeasureDate(LocalDateTime.of(2014, 4, 11, 11, 11, 11));
        stat2.setWeight(67.0);

        return stat2;
    }

    private Training createTraining1(Person p1) {
        Training training = new Training();
        training.setType(entityManager.find(TrainingType.class, 1));
        training.setTrainee(p1);
        training.setTrainingStart(LocalDateTime.of(2014, 3, 28, 18, 18, 18));
        training.setTrainingStop(LocalDateTime.of(2014, 3, 28, 19, 19, 19));
        return training;
    }

    private Training createTraining2(Person p1) {
        Training training = new Training();
        training.setType(entityManager.find(TrainingType.class, 2));
        training.setTrainee(p1);
        training.setTrainingStart(LocalDateTime.of(2014, 3, 31, 18, 0, 0));
        training.setTrainingStop(LocalDateTime.of(2014, 3, 31, 19, 0, 0));
        return training;
    }

    private void persistTraining(Training training) {
        entityManager.getTransaction().begin();
        entityManager.persist(training);
        entityManager.getTransaction().commit();
    }

    private void updateTraining(Training training) {
        entityManager.getTransaction().begin();
        entityManager.merge(training);
        entityManager.getTransaction().commit();
    }

    private void checkTraining(Training training, Training expected) {
        assertEquals(expected.getType(), training.getType());
        assertEquals(expected.getTrainee(), training.getTrainee());
        assertEquals(expected.getTrainingStart(), training.getTrainingStart());
        assertEquals(expected.getTrainingStop(), training.getTrainingStop());
    }

    private Exercise createExercise1(Training training) {
        Exercise exercise = new Exercise();
        exercise.setTraining(training);
        exercise.setActivity(entityManager.find(Activity.class, 23));

        ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setReps(6);
        exerciseSet.setResult(65.6);
        exerciseSet.setNote("note");
        exerciseSet.setDuration("PT1M");

        ExerciseSet exerciseSet2 = new ExerciseSet();
        exerciseSet2.setReps(5);
        exerciseSet2.setResult(65.6);
        exerciseSet2.setNote("note1");
        exerciseSet2.setDuration("PT1M1s");

        exercise.setExerciseSets(Arrays.asList(exerciseSet, exerciseSet2));

        return exercise;
    }

    private Exercise createExercise2(Training training) {
        Exercise exercise = new Exercise();
        exercise.setTraining(training);
        exercise.setActivity(entityManager.find(Activity.class, 24));

        ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setReps(6);
        exerciseSet.setResult(50.0);
        exerciseSet.setNote("note2");

        ExerciseSet exerciseSet2 = new ExerciseSet();
        exerciseSet2.setReps(5);
        exerciseSet2.setResult(50.0);
        exerciseSet2.setNote("note3");

        exercise.setExerciseSets(Arrays.asList(exerciseSet, exerciseSet2));

        return exercise;
    }

    private Exercise createExercise3(Training training) {
        Exercise exercise = new Exercise();
        exercise.setTraining(training);
        exercise.setActivity(entityManager.find(Activity.class, 1));

        ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setDuration("PT30M");
        exerciseSet.setResult(5.6);

        exercise.setExerciseSets(Arrays.asList(exerciseSet));

        return exercise;
    }

    private void persistExercise(Exercise exercise) {
        entityManager.getTransaction().begin();
        entityManager.persist(exercise);
        entityManager.getTransaction().commit();
    }

}
