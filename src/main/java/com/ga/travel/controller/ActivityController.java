package com.ga.travel.controller;

import com.ga.travel.model.Activity;
import com.ga.travel.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class ActivityController {

    private ActivityService activityService;

    @PostMapping("/trips/{tripId}/activities")
    public Activity createActivity(
            @PathVariable(value = "tripId") Long tripId,
            @RequestBody Activity activityObject
    ) {
        System.out.println("Calling createActivity ==>");
        return activityService.createActivity(tripId, activityObject);
    }

    @GetMapping("/trips/{tripId}/activities")
    public List<Activity> getActivities(@PathVariable(value = "tripId") Long tripId) {
        System.out.println("calling getActivities ==>");
        return activityService.getActivities(tripId);
    }

    @GetMapping("/trips/{tripId}/activities/{activityId}")
    public Activity getActivity(
            @PathVariable(value = "tripId") Long tripId, @PathVariable(value = "activityId") Long activityId) {
        System.out.println("calling getActivity ==>");
        return activityService.getActivity(tripId, activityId);
    }

    @PutMapping("/trips/{tripId}/activities/{activityId}")
    public Activity updateActivity(@PathVariable(value = "tripId") Long tripId,
                                   @PathVariable(value = "activityId") Long activityId,
                                   @RequestBody Activity activityObject) {
        System.out.println("calling updateActivity ==>");
        return activityService.updateActivity(tripId, activityId, activityObject);
    }

    @DeleteMapping("/trips/{tripId}/activities/{activityId}")
    public ResponseEntity<HashMap<String, String>> deleteActivity(
            @PathVariable(value = "tripId") Long tripId, @PathVariable(value = "activityId") Long activityId) {
        System.out.println("calling deleteActivity ==>");
        activityService.deleteActivity(tripId, activityId);
        HashMap<String, String> responseMessage = new HashMap<>();
        responseMessage.put("status", "activity with id: " + activityId + " was successfully deleted.");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}
