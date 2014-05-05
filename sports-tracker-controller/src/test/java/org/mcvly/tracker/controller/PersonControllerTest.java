package org.mcvly.tracker.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mcvly.tracker.core.Exercise;
import org.mcvly.tracker.core.Person;
import org.mcvly.tracker.core.PersonStats;
import org.mcvly.tracker.core.Training;
import org.mcvly.tracker.core.TrainingType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

/**
 * @author mcvly
 * @since 24.04.14
 */
public class PersonControllerTest extends AbstractControllerTest {

    @Value("classpath:trn.json")
    private Resource trainingsJsonData;

    @Test
    public void testPersonInfo() throws Exception {

        when(sportTrackerServiceMock.getPersonInformation(anyInt())).thenReturn(new Person("mcvly", LocalDate.now(), 173));

        mockMvc.perform(get("/person/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("mcvly")))
                ;

        verify(sportTrackerServiceMock, times(1)).getPersonInformation(1);
    }

    @Test
    public void testInfoUnknownPerson() throws Exception {

        when(sportTrackerServiceMock.getPersonInformation(anyInt())).thenReturn(null);

        mockMvc.perform(get("/person/{id}", 2))
                .andExpect(status().isNotFound())
        ;

        verify(sportTrackerServiceMock, times(1)).getPersonInformation(2);
    }

    @Test
    public void testPersonStats() throws Exception {

        LocalDateTime now = LocalDateTime.now();

        when(sportTrackerServiceMock.getPersonStats(eq(1))).thenReturn(Arrays.asList(
                new PersonStats(now, 64.2),
                new PersonStats(LocalDateTime.now().minusWeeks(2), 64.4),
                new PersonStats(LocalDateTime.now().minusWeeks(1), 64.3)
        ));

        mockMvc.perform(get("/person/{id}/stats", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.", hasSize(3)))
                .andExpect(jsonPath("$[0].weight", is(64.2)))
                .andExpect(jsonPath("$[0].measureDate", is(now.toString())))
        ;
    }

    @Test
    public void testTrainingInfo() throws Exception {
        LocalDate now = LocalDate.now();

        TrainingType some = new TrainingType();

        Training t1 = new Training(some, now.minusDays(2).atTime(13, 13), now.minusDays(2).atTime(15, 13));
        Training t2 = new Training(some, now.atTime(13, 13), now.atTime(15, 13));

        when(sportTrackerServiceMock.getTrainingInfos(eq(1), eq(now))).thenReturn(Arrays.asList(t1));
        when(sportTrackerServiceMock.getTrainingInfos(eq(1), eq(null))).thenReturn(Arrays.asList(t1, t2));

        mockMvc.perform(get("/person/{id}/traininfo?since=2014-04-30", 1))
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
    }

    @Test
    public void testTrainings() throws Exception {
        when(sportTrackerServiceMock.getTrainingsWithExercises(eq(1), eq(1), eq(10))).thenReturn(trainingsFromFile().subList(0, 10));
        when(sportTrackerServiceMock.getTrainingsWithExercises(eq(1), eq(2), eq(10))).thenReturn(trainingsFromFile().subList(10, 11));

        mockMvc.perform(get("/person/{id}/trainings?page=1&size=10", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.", hasSize(10)))
        ;

        mockMvc.perform(get("/person/{id}/trainings?page=2&size=10", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.", hasSize(1)))
        ;
    }

    public List<Training> trainingsFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Training[] ts = objectMapper.readValue(trainingsJsonData.getFile(), Training[].class);

        return Arrays.asList(ts);
    }
}
