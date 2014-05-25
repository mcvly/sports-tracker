package org.mcvly.tracker.controller.config;

import org.junit.Test;
import org.mcvly.tracker.controller.AbstractControllerTest;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mcvly
 * @since 25.05.14
 */
public class ActivityControllerTest extends AbstractControllerTest {

    @Test
    public void testTrainingTypes() throws Exception {
        when(sportTrackerServiceMock.getActivities()).thenReturn(new ArrayList<>(activities.values()));

        mockMvc.perform(get("/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.", hasSize(40)))
                .andExpect(jsonPath("$[0].name", is("Бег на тренажере")))
                .andExpect(jsonPath("$[0].type.code", is("RUNNING")))
                .andExpect(jsonPath("$[0].subType", nullValue()))
        ;
    }

}
