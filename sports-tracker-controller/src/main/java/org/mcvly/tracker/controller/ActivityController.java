package org.mcvly.tracker.controller;

import org.mcvly.tracker.core.Activity;
import org.mcvly.tracker.service.STServiceException;
import org.mcvly.tracker.service.SportTrackerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mcvly
 * @since 24.05.14
 */
@RestController
public class ActivityController {

    @Resource
    private SportTrackerService sportTrackerService;

    @RequestMapping(value = "activities", method = RequestMethod.GET)
    public List<Activity> activities() {
        return sportTrackerService.getActivities();
    }

    @RequestMapping(value = "activities", method = RequestMethod.POST)
    @ResponseBody
    public Activity addActivity(@RequestBody Activity activity) {
        return sportTrackerService.addActivity(activity);
    }

    @RequestMapping(value = "activities/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateActivity(@PathVariable("id") Integer activityId, @RequestBody Activity activity) {
        try {
            sportTrackerService.updateActivity(activity);
        } catch (STServiceException e) {
            throw new ResourceNotFoundException();
        }
    }
}
