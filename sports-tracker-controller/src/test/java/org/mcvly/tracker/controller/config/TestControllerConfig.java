package org.mcvly.tracker.controller.config;

import org.mcvly.tracker.service.SportTrackerService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dell
 * @since 21.04.2014
 */
@Configuration
public class TestControllerConfig {

    @Bean
    public SportTrackerService sportTrackerServiceMock() {
        return Mockito.mock(SportTrackerService.class);
    }

}
