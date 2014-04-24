package org.mcvly.tracker.controller.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author mcvly
 * @since 16.04.14
 */
@Configuration
@EntityScan("org.mcvly.tracker.core")
@EnableJpaRepositories("org.mcvly.tracker.repository")
@EnableTransactionManagement
public class ServiceConfig {

}
