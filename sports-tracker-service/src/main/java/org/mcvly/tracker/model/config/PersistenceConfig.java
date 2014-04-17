package org.mcvly.tracker.model.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mcvly
 * @since 16.04.14
 */
@Configuration
@EntityScan("org.mcvly.tracker.core")
@EnableJpaRepositories("org.mcvly.tracker.model.repository")
@EnableTransactionManagement
public class PersistenceConfig {

}
