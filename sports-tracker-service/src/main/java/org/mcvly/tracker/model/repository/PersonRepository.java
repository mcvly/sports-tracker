package org.mcvly.tracker.model.repository;

import org.mcvly.tracker.core.Person;
import org.mcvly.tracker.core.PersonStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mcvly
 * @since 31.03.14
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("select new Person(p.name, p.birth, p.height) from Person p where p.id = :personId")
    public Person getInfo(@Param("personId") Integer id);

}
