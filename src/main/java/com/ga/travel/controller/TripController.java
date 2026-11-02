package com.ga.travel.controller;

import com.ga.travel.model.Trip;
import com.ga.travel.service.TripService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class TripController {

    private TripService tripService;

    // CRUD
    // C - Create - HTTP POST - To create a record (trip)
    // Sent as form-data because it can include a cover photo
    @PostMapping("/trips")
    public Trip createTrip(
            @RequestParam("name") String name,
            @RequestParam("destination") String destination,
            @RequestParam("description") String description,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        System.out.println("Calling createTrip ==> ");
        return tripService.createTrip(name, destination, description, startDate, endDate, image);
    }

    // R - Read - HTTP GET - To read all records / record by id
    @GetMapping("/trips")
    public List<Trip> getTrips() {
        System.out.println("calling getTrips() ==> ");
        return tripService.getTrips();
    }

    @GetMapping(path = "/trips/{tripId}")
    public Trip getTrip(@PathVariable Long tripId) {
        System.out.println("calling getTrip ==>");
        return tripService.getTrip(tripId);
    }

    // U - Update - HTTP PUT - To update a record
    @PutMapping("/trips/{tripId}")
    public Trip updateTrip(@PathVariable(value = "tripId") Long tripId, @RequestBody Trip tripObject) {
        System.out.println("calling updateTrip ==>");
        return tripService.updateTrip(tripId, tripObject);
    }

    // D - Delete - HTTP DELETE - To remove a record
    @DeleteMapping("/trips/{tripId}")
    public Trip deleteTrip(@PathVariable(value = "tripId") Long tripId) {
        System.out.println("calling deleteTrip ==>");
        return tripService.deleteTrip(tripId);
    }

}
