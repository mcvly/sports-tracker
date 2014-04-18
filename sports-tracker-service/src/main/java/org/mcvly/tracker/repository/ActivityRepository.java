package org.mcvly.tracker.repository;

import org.mcvly.tracker.core.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mcvly
 * @since 08.04.14
 */
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

}
