package org.mcvly.tracker.repository;

import org.mcvly.tracker.core.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author mcvly
 * @since 31.03.14
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("select new Person(p.name, p.birth, p.height) from Person p where p.id = :personId")
    public Person getInfo(@Param("personId") Integer id);

}
