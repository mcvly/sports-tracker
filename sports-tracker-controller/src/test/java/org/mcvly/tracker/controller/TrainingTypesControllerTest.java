package org.mcvly.tracker.controller;

import org.junit.Test;
import org.mcvly.tracker.service.STServiceException;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mcvly
 * @since 24.05.14
 */
public class TrainingTypesControllerTest extends AbstractControllerTest {

    @Test
    public void testTrainingTypes() throws Exception {
        when(sportTrackerServiceMock.getTrainingTypes()).thenReturn(new ArrayList<>(trainingTypes.values()));

        mockMvc.perform(get("/training-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.", hasSize(4)))
                .andExpect(jsonPath("$[0].code", is("GYM")))
                .andExpect(jsonPath("$[0].name", is("Тренажерка")))
                ;
    }

    @Test
    public void testTrainingSubTypesNotFound() throws Exception {
        when(sportTrackerServiceMock.getTrainingSubtypes(2)).thenThrow(new STServiceException());

        mockMvc.perform(get("/training-types/{id}/subtypes", 2))
                .andExpect(status().isNotFound())
        ;

        verify(sportTrackerServiceMock, times(1)).getTrainingSubtypes(2);

    }

    @Test
    public void testTrainingSubTypes() throws Exception {
        when(sportTrackerServiceMock.getTrainingSubtypes(1)).thenReturn(new ArrayList<>(trainingSubTypes.values()));

        mockMvc.perform(get("/training-types/{id}/subtypes", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.", hasSize(8)))
                .andExpect(jsonPath("$[0].type.code", is("GYM")))
                .andExpect(jsonPath("$[0].type.name", is("Тренажерка")))
                .andExpect(jsonPath("$[0].name", is("базовые")))
        ;

        verify(sportTrackerServiceMock, times(1)).getTrainingSubtypes(1);

    }

}
