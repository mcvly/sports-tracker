package org.mcvly.tracker.test;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.mcvly.tracker.model.Activity;
import org.mcvly.tracker.model.Person;
import org.mcvly.tracker.model.PersonStats;
import org.mcvly.tracker.model.TrainingSubType;
import org.mcvly.tracker.model.TrainingType;

/**
 * @author <a href="mailto:RMalyona@luxoft.com">Ruslan Malyona</a>
 * @since 11.02.14
 */
public class ModelTest {

    private static EntityManagerFactory entityManagerFactory;

    static {
        try {
            PersistenceProvider provider = new HibernatePersistenceProvider();
            entityManagerFactory = provider.createEntityManagerFactory("primary", new HashMap());
        } catch (Exception e) {
            System.err.println("Entity Manager creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void main(String[] args) {
        EntityManager em = entityManagerFactory.createEntityManager();

//        testPerson(em);
        testActivity(em);

        em.close();

        entityManagerFactory.close();
    }

    private static void testActivity(EntityManager em) {
        em.getTransaction().begin();

        TrainingSubType subType = new TrainingSubType();
        subType.setName("грудь");
        subType.setType(TrainingType.GYM);

        Activity activity = new Activity();
        activity.setType(TrainingType.GYM);
        activity.setSubType(subType);
        activity.setName("Гантели в стороны");
        activity.setDescription("---");

        em.persist(subType);
        em.persist(activity);

        em.getTransaction().commit();
        Activity p1 = em.find(Activity.class, activity.getId());
        System.out.println(p1);

    }

    private static void testPerson(EntityManager em) {
        em.getTransaction().begin();

        Person me = new Person();
        me.setBirth(new GregorianCalendar(1990, 2, 10).getTime());
        me.setName("mcvly");
        me.setHeight(173);

        PersonStats stat1 = new PersonStats();
        stat1.setMeasureDate(new GregorianCalendar(2014, 1, 10).getTime());
        stat1.setWeight(68.1);

        PersonStats stat2 = new PersonStats();
        stat2.setMeasureDate(new GregorianCalendar(2014, 1, 11).getTime());
        stat2.setWeight(67.7);

        me.setStats(Arrays.asList(stat1, stat2));

        em.persist(me);

        em.getTransaction().commit();

        Person p1 = em.find(Person.class, me.getId());
        System.out.println(p1);
    }

}
