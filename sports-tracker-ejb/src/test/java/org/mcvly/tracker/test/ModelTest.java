package org.mcvly.tracker.test;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.jdbc.AbstractWork;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mcvly.tracker.model.TrainingType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    private static EntityTransaction transaction;

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
        transaction = entityManager.getTransaction();
        populateData();
    }

    private void populateData() {

        try {
            Session session = entityManager.unwrap(Session.class);
            session.doWork(new AbstractWork() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    try {
                        transaction.begin();

                        connection.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
                        IDatabaseConnection dbUnitCon = new DatabaseConnection(connection);
                        IDataSet dataSet = getDataSet();
                        DatabaseOperation.CLEAN_INSERT.execute(dbUnitCon, dataSet);

                        transaction.commit();
                    } catch (URISyntaxException | DatabaseUnitException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private IDataSet getDataSet() throws URISyntaxException, DataSetException {
        return new CsvDataSet(new File(ClassLoader.getSystemResource("sql").toURI()));
    }

    @Test
    public void test() {
        entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
        TrainingType t1 = entityManager.find(TrainingType.class, 1);
        System.out.println(t1);
    }
}
