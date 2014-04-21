package org.mcvly.tracker.controller;

import org.mcvly.tracker.config.ServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author mcvly
 * @since 18.04.14
 */
@Configuration
@EnableAutoConfiguration(exclude = ServiceConfig.class)
@ComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}