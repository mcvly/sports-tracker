package org.mcvly.tracker.controller.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author mcvly
 * @since 18.04.14
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("org.mcvly.tracker.controller")
@Import(WebConfig.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}