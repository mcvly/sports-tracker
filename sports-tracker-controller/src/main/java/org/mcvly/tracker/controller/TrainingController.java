package org.mcvly.tracker.controller;

import org.mcvly.tracker.core.Exercise;
import org.mcvly.tracker.core.Training;
import org.mcvly.tracker.service.STServiceException;
import org.mcvly.tracker.service.SportTrackerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @author mcvly
 * @since 03.07.14
 */
@RestController
public class TrainingController {

    @Resource
    private SportTrackerService sportTrackerService;

    @RequestMapping(value = "person/{id}/traininfo", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public List<Training> trainingsInfo(@PathVariable("id") Integer personId,
                                        @RequestParam(value = "since", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since) {
        return sportTrackerService.getTrainingInfos(personId, since);
    }

    @RequestMapping(value = "person/{id}/trainings", params = { "page", "size" }, method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public List<Training> trainings(@PathVariable("id") Integer personId,
                                    @RequestParam(value = "page", required = false) int page,
                                    @RequestParam(value = "size", required = false) int size) {
        return sportTrackerService.getTrainingsWithExercises(personId, page, size);
    }

    @RequestMapping(value = "person/{id}/trainings", method = RequestMethod.POST)
    @ResponseBody
    public Training addTraining(@PathVariable("id") Integer personId,
                                @RequestBody Training training) {
        try {
            return sportTrackerService.addTraining(personId, training);
        } catch (STServiceException e) {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(value = "training/{id}", method = RequestMethod.GET)
    public Training getById(@PathVariable("id") Long trainingId) {
        try {
            return sportTrackerService.getTraining(trainingId);
        } catch (STServiceException e) {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(value = "training/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addExerciseToTraining(@PathVariable("id") Long trainingId, @RequestBody Exercise exercise) {
        try {
            sportTrackerService.addTrainingExercise(trainingId, exercise);
        } catch (STServiceException e) {
            throw new ResourceNotFoundException();
        }
    }

}
