package org.mcvly.tracker.controller;

import org.junit.Test;
import org.mcvly.tracker.controller.AbstractControllerTest;
import org.mcvly.tracker.core.Activity;
import org.mockito.Matchers;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mcvly
 * @since 25.05.14
 */
public class ActivityControllerTest extends AbstractControllerTest {

    @Test
    public void testGetActivities() throws Exception {
        when(sportTrackerServiceMock.getActivities()).thenReturn(new ArrayList<>(activities.values()));

        mockMvc.perform(get("/activities"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.", hasSize(40)))
                .andExpect(jsonPath("$[0].name", is("Бег на тренажере")))
                .andExpect(jsonPath("$[0].type.code", is("RUNNING")))
        ;
    }

    @Test
    public void testAddActivity() throws Exception {

        Activity myActivity = activities.get(1);

        when(sportTrackerServiceMock.addActivity(myActivity)).thenReturn(myActivity);

        String json = TestUtil.convertObjectToJsonObject(myActivity);
        mockMvc.perform(post("/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
        ;

        verify(sportTrackerServiceMock, times(1)).addActivity(eq(myActivity));
    }

    @Test
    public void testUpdateActivity() throws Exception {

        Activity myActivity = activities.get(1);
        myActivity.setName("new name");

        String json = TestUtil.convertObjectToJsonObject(myActivity);
        mockMvc.perform(put("/activities/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andDo(print())
                .andExpect(status().isNoContent())
        ;

        verify(sportTrackerServiceMock, times(1)).updateActivity(eq(myActivity));
    }

}
