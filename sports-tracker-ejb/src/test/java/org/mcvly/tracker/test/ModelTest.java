package org.mcvly.tracker.test;

import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.mcvly.tracker.model.Person;

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

        em.getTransaction().begin();

        Person me = new Person();
        me.setBirth(new GregorianCalendar(1990, 2, 10).getTime());
        me.setName("mcvly");
        me.setHeight(173);

        em.persist(me);

        em.getTransaction().commit();
        em.close();

        entityManagerFactory.close();
    }

}
