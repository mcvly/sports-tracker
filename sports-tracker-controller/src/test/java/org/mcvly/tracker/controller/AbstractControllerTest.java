package org.mcvly.tracker.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mcvly.tracker.controller.config.Application;
import org.mcvly.tracker.service.SportTrackerService;
import org.mockito.Mockito;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * @author mcvly
 * @since 24.04.14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;

    @Resource
    protected SportTrackerService sportTrackerServiceMock;

    @Resource
    protected WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        Mockito.reset(sportTrackerServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

}
