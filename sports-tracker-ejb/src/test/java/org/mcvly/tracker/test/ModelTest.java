package org.mcvly.tracker.test;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:RMalyona@luxoft.com">Ruslan Malyona</a>
 * @since 11.02.14
 */
public class ModelTest {

    private static final String TEST_UNIT = "test";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
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
    public void initTransaction() {
        populateData();
    }

    private void populateData() {

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
//                new CsvDataSet(new File(ClassLoader.getSystemResource("sql").toURI())),
                new FlatXmlDataSetBuilder().build(ClassLoader.getSystemResourceAsStream("initDataSet.xml"))
        );
    }

    @Test
    public void testRead() throws ParseException {

        Person p1;

        entityManager.getTransaction().begin();
        p1 = entityManager.find(Person.class, 1);
        Exercise e1 = entityManager.find(Exercise.class, 1L);
        System.out.println(e1);
        entityManager.getTransaction().commit();

//        checkStats(p1);
//        checkTrainings(p1);

    }

    @Test
    public void testCreate() {
        entityManager.getTransaction().begin();

        Training training = new Training();

        Exercise exercise = new Exercise();
        exercise.setActivity(entityManager.find(Activity.class, 1));
        ExerciseSet set1 = new ExerciseSet();
        set1.setDuration("PT2M");
        set1.setReps(2);
        exercise.addSet(set1);
        ExerciseSet set2 = new ExerciseSet();
        set2.setResult(55.0);
        set2.setReps(4);
        exercise.addSet(set2);

        Exercise exercise2 = new Exercise();
        exercise.setActivity(entityManager.find(Activity.class, 1));
        ExerciseSet set3 = new ExerciseSet();
        set1.setDuration("PT2M");
        set1.setReps(3);
        exercise.addSet(set1);
        ExerciseSet set4 = new ExerciseSet();
        set2.setResult(55.0);
        set2.setReps(5);
        exercise.addSet(set2);

        training.setExercises(Arrays.asList(exercise, exercise2));

        entityManager.persist(training);

        entityManager.getTransaction().commit();

        System.out.println(entityManager.find(Training.class, 1L).getExercises());

        entityManager.unwrap(Session.class).doWork(connection -> {
            try {
                DatabaseConnection dbConnection = new DatabaseConnection(connection);
                FlatXmlDataSet.write(dbConnection.createDataSet(), new FileOutputStream("full.xml"));

            } catch (DatabaseUnitException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void checkStats(Person p1) {
        List<PersonStats> stats = p1.getStats();
        assertEquals(2, stats.size());

        PersonStats stat1 = new PersonStats();
        stat1.setMeasureDate(new GregorianCalendar(2014, 1, 10).getTime());
        stat1.setWeight(68.1);

        PersonStats stat2 = new PersonStats();
        stat2.setMeasureDate(new GregorianCalendar(2014, 1, 11).getTime());
        stat2.setWeight(67.7);

        assertEquals(stat1, stats.get(0));
        assertEquals(stat2, stats.get(1));
    }


    private void checkTrainings(Person p1) throws ParseException {
        List<Training> trainings = p1.getTrainings();
        assertEquals(2, trainings.size());

        Training training = trainings.get(0);
        assertEquals(new Integer(1), training.getType().getId());
        assertEquals(p1,training.getTrainee());
        assertEquals(new SimpleDateFormat(DATE_FORMAT).parse("2014-03-25 23:41:13.420"), training.getTrainingStart());
        assertEquals(new SimpleDateFormat(DATE_FORMAT).parse("2014-03-26 00:56:13.423"), training.getTrainingStop());
        checkExercises(training);

        training = trainings.get(1);
        assertEquals(1, training.getExercises().size());
        assertEquals(new Integer(1), training.getExercises().get(0).getActivity().getId());
        assertEquals(1, training.getExercises().get(0).getExerciseSets().size());
    }

    private void checkExercises(Training training) {
        List<Exercise> exercises = training.getExercises();
        assertEquals(2, exercises.size());

        Exercise exercise = exercises.get(0);
        assertEquals(training, exercise.getTraining());
        assertEquals(new Integer(23), exercise.getActivity().getId());
        assertEquals(2, exercise.getExerciseSets().size());
        ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setReps(6);
        exerciseSet.setResult(65.6);
        exerciseSet.setNote("note");
        exerciseSet.setDuration("PT1M");
        assertEquals(exerciseSet, exercise.getExerciseSets().get(0));
        exerciseSet = new ExerciseSet();
        exerciseSet.setReps(5);
        exerciseSet.setResult(65.6);
        exerciseSet.setNote("note1");
        exerciseSet.setDuration("PT1M1s");
        assertEquals(exerciseSet, exercise.getExerciseSets().get(1));

        exercise = exercises.get(1);
        assertEquals(training, exercise.getTraining());
        assertEquals(new Integer(24), exercise.getActivity().getId());
        assertEquals(2, exercise.getExerciseSets().size());
        exerciseSet = new ExerciseSet();
        exerciseSet.setReps(6);
        exerciseSet.setResult(50.0);
        exerciseSet.setNote("note2");
        assertEquals(exerciseSet, exercise.getExerciseSets().get(0));
        exerciseSet = new ExerciseSet();
        exerciseSet.setReps(5);
        exerciseSet.setResult(50.0);
        exerciseSet.setNote("note3");
        assertEquals(exerciseSet, exercise.getExerciseSets().get(1));
    }
}
