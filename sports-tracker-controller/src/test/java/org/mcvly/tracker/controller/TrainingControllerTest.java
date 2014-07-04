package org.mcvly.tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mcvly.tracker.core.Exercise;
import org.mcvly.tracker.core.Training;
import org.mcvly.tracker.core.TrainingType;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author mcvly
 * @since 03.07.14
 */
public class TrainingControllerTest extends AbstractControllerTest {

    @Value("classpath:trn.json")
    private Resource trainingsJsonData;

    private List<Training> preparedTrainings;

    @Test
    public void testTrainingInfo() throws Exception {
        LocalDate now = LocalDate.now();

        TrainingType some = new TrainingType();

        Training t1 = new Training(some, now.minusDays(2).atTime(13, 13), now.minusDays(2).atTime(15, 13));
        Training t2 = new Training(some, now.atTime(13, 13), now.atTime(15, 13));

        when(sportTrackerServiceMock.getTrainingInfos(eq(1), eq(now))).thenReturn(Arrays.asList(t1));
        when(sportTrackerServiceMock.getTrainingInfos(eq(1), eq(null))).thenReturn(Arrays.asList(t1, t2));

        mockMvc.perform(get("/person/{id}/traininfo?since=" + now, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.", hasSize(1)))
        ;

        mockMvc.perform(get("/person/{id}/traininfo", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.", hasSize(2)))
        ;

        mockMvc.perform(get("/person/{id}/traininfo", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.", hasSize(0)))
        ;

        mockMvc.perform(get("/person/{id}/traininfo?since=abcc", 1))
                .andExpect(status().isBadRequest())
        ;

        mockMvc.perform(get("/person/{id}/traininfo?ggg=abcc", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.", hasSize(2)))
        ;

        verify(sportTrackerServiceMock, times(1)).getTrainingInfos(eq(1), eq(now));
        verify(sportTrackerServiceMock, times(1)).getTrainingInfos(eq(2), Matchers.any());

    }

    @Test
    public void testTrainings() throws Exception {
        when(sportTrackerServiceMock.getTrainingsWithExercises(eq(1), eq(1), eq(10))).thenReturn(getPreparedTrainings().subList(0, 10));
        when(sportTrackerServiceMock.getTrainingsWithExercises(eq(1), eq(2), eq(10))).thenReturn(getPreparedTrainings().subList(10, 11));

        mockMvc.perform(get("/person/{id}/trainings", 1)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.", hasSize(10)))
        ;

        mockMvc.perform(get("/person/{id}/trainings", 1)
                        .param("page", "2")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.", hasSize(1)))
        ;

        verify(sportTrackerServiceMock, times(2)).getTrainingsWithExercises(eq(1), anyInt(), anyInt());

    }

    @Test
    public void testAddTraining() throws Exception {
        when(sportTrackerServiceMock.addTraining(eq(1), Matchers.any())).thenReturn(getPreparedTrainings().get(0));

        String json = TestUtil.convertObjectToJsonObject(getPreparedTrainings().get(0));
        mockMvc.perform(post("/person/{id}/trainings", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
        ;

        verify(sportTrackerServiceMock, times(1)).addTraining(eq(1), Matchers.any(Training.class));
    }

    @Test
    public void testGetTraining() throws Exception {
        when(sportTrackerServiceMock.getTraining(eq(1L))).thenReturn(getPreparedTrainings().get(0));

        mockMvc.perform(get("/training/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
        ;

        verify(sportTrackerServiceMock, times(1)).getTraining(eq(1L));
    }

    @Test
    public void testAddExerciseToTraining() throws Exception {
        String json = objectMapper.writeValueAsString(getPreparedTrainings().get(1).getExercises().get(0));

        mockMvc.perform(put("/training/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(status().isNoContent())
        ;

        verify(sportTrackerServiceMock, times(1)).addTrainingExercise(eq(1L), Matchers.any(Exercise.class));

    }

    private List<Training> getPreparedTrainings() throws IOException {

        if (preparedTrainings == null) {
            Training[] ts = objectMapper.readValue(trainingsJsonData.getFile(), Training[].class);
            preparedTrainings = Arrays.asList(ts);
        }

        return preparedTrainings;
    }
}
