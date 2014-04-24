package org.mcvly.tracker.controller;

import org.junit.Test;
import org.mcvly.tracker.core.Person;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mcvly
 * @since 24.04.14
 */
public class PersonControllerTest extends AbstractControllerTest {

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

}
