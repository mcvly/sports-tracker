package org.mcvly.tracker.controller;

import org.mcvly.tracker.core.TrainingSubType;
import org.mcvly.tracker.core.TrainingType;
import org.mcvly.tracker.service.STServiceException;
import org.mcvly.tracker.service.SportTrackerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mcvly
 * @since 24.05.14
 */
@RestController
@RequestMapping(value="/training-types")
public class TrainingTypesController {

    @Resource
    private SportTrackerService sportTrackerService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<TrainingType> trainingTypes() {
        return sportTrackerService.getTrainingTypes();
    }

    @RequestMapping(value = "{id}/subtypes", method = RequestMethod.GET)
    public List<TrainingSubType> trainingSubTypes(@PathVariable("id") Integer trainingTypeId) {
        try {
            return sportTrackerService.getTrainingSubtypes(trainingTypeId);
        } catch (STServiceException e) {
            throw new ResourceNotFoundException();
        }
    }
}
