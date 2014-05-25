package org.mcvly.tracker.controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mcvly.tracker.controller.config.Application;
import org.mcvly.tracker.core.Activity;
import org.mcvly.tracker.core.TrainingSubType;
import org.mcvly.tracker.core.TrainingType;
import org.mcvly.tracker.service.SportTrackerService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mcvly
 * @since 24.04.14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;

    protected Map<Integer, TrainingType> trainingTypes = new HashMap<>();
    protected Map<Integer, TrainingSubType> trainingSubTypes = new HashMap<>();
    protected Map<Integer, Activity> activities = new HashMap<>();

    @Resource
    protected SportTrackerService sportTrackerServiceMock;

    @Resource
    protected WebApplicationContext webApplicationContext;

    @Value("${training.types}")
    private File trainingTypeCsv;

    @Value("${training.subtypes}")
    private File trainingSubTypeCsv;

    @Value("${activities}")
    private File activityCsv;

    @PostConstruct
    public void setupObjectsFromCSV() throws IOException {
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);

        //training types
        MappingIterator<Object[]> it = mapper.reader(Object[].class).readValues(trainingTypeCsv);
        while (it.hasNext()) {
            TrainingType t = typeFromRawData(it.next());
            if (t != null) {
                trainingTypes.put(t.getId(), t);
            }
        }

        it = mapper.reader(Object[].class).readValues(trainingSubTypeCsv);
        while(it.hasNext()) {
            TrainingSubType st = subTypeFromRawData(it.next());
            if (st != null) {
                trainingSubTypes.put(st.getId(), st);
            }
        }

        it = mapper.reader(Object[].class).readValues(activityCsv);
        while(it.hasNext()) {
            Activity ac = activityFromRawData(it.next());
            if (ac != null) {
                activities.put(ac.getId(), ac);
            }
        }
    }

    @Before
    public void setup() {
        Mockito.reset(sportTrackerServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private TrainingType typeFromRawData(Object[] data) {
        if ("id".equals(data[0])) {
            return null;
        }
        Integer id = Integer.parseInt(String.valueOf(data[0]));
        String code = String.valueOf(data[1]);
        String name = String.valueOf(data[2]);

        return new TrainingType(id, code, name);
    }

    private TrainingSubType subTypeFromRawData(Object[] data) {
        if ("id".equals(data[0])) {
            return null;
        }
        Integer id = Integer.parseInt(String.valueOf(data[0]));
        String name = String.valueOf(data[1]);
        Integer typeId = Integer.parseInt(String.valueOf(data[2]));

        return new TrainingSubType(id, trainingTypes.get(typeId), name);
    }

    private Activity activityFromRawData(Object[] data) {
        if ("id".equals(data[0])) {
            return null;
        }
        Integer id = Integer.parseInt(String.valueOf(data[0]));
        String name = String.valueOf(data[1]);
        TrainingType type = trainingTypes.get(Integer.parseInt(String.valueOf(data[2])));
        TrainingSubType subType = !"null".equals(data[3]) ? trainingSubTypes.get(Integer.parseInt(String.valueOf(data[3]))) : null;

        return new Activity(id, name, type, subType);
    }
}
