package org.mcvly.tracker.controller;

import org.mcvly.tracker.core.Activity;
import org.mcvly.tracker.service.SportTrackerService;
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
public class ActivityController {

    @Resource
    private SportTrackerService sportTrackerService;

    @RequestMapping(value = "activities", method = RequestMethod.GET)
    public List<Activity> activities() {
        return sportTrackerService.getActivities();
    }

}
