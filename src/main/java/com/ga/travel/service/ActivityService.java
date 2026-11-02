package com.ga.travel.service;

import com.ga.travel.exception.InformationNotFoundException;
import com.ga.travel.model.Activity;
import com.ga.travel.model.Trip;
import com.ga.travel.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ActivityService {

    private ActivityRepository activityRepository;
    private TripService tripService;

    public Activity createActivity(Long tripId, Activity activity) {
        System.out.println("Service calling createActivity ==>");
        // getTrip() already checks the trip belongs to the logged-in user
        Trip trip = tripService.getTrip(tripId);
        activity.setTrip(trip);
        activity.setUser(TripService.getCurrentLoggedInUser());
        return activityRepository.save(activity);
    }

    // Sorted by day, then time, like a real itinerary
    public List<Activity> getActivities(Long tripId) {
        System.out.println("service calling getActivities ==>");
        tripService.getTrip(tripId);
        return activityRepository.findByTripIdOrderByDayAscTimeAsc(tripId);
    }

    public Activity getActivity(Long tripId, Long activityId) {
        tripService.getTrip(tripId);
        return activityRepository.findByIdAndTripId(activityId, tripId)
                .orElseThrow(() ->
                        new InformationNotFoundException(
                                "Activity with id " + activityId +
                                        " not found in trip " + tripId));
    }

    public Activity updateActivity(Long tripId, Long activityId, Activity updatedActivity) {

        Activity activity = getActivity(tripId, activityId);

        activity.setName(updatedActivity.getName());
        activity.setDay(updatedActivity.getDay());
        activity.setTime(updatedActivity.getTime());
        activity.setLocation(updatedActivity.getLocation());
        activity.setCost(updatedActivity.getCost());
        activity.setNotes(updatedActivity.getNotes());
        activity.setBooked(updatedActivity.isBooked());

        return activityRepository.save(activity);
    }

    public void deleteActivity(Long tripId, Long activityId) {

        Activity activity = getActivity(tripId, activityId);

        activityRepository.delete(activity);
    }

}
