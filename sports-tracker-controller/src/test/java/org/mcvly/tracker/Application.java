package org.mcvly.tracker;

import org.mcvly.tracker.config.ServiceTestConfiguration;
import org.mcvly.tracker.service.SportTrackerService;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author mcvly
 * @since 18.04.14
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import(ServiceTestConfiguration.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}