package com.ga.travel.service;

import com.ga.travel.exception.InformationExistException;
import com.ga.travel.exception.InformationNotFoundException;
import com.ga.travel.model.Trip;
import com.ga.travel.model.User;
import com.ga.travel.repository.TripRepository;
import com.ga.travel.security.MyUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TripService {
    private final String UPLOAD_DIR = "uploads/";

    private TripRepository tripRepository;

    public static User getCurrentLoggedInUser() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }

    public Trip createTrip(
            String name,
            String destination,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            MultipartFile image) {

        System.out.println("Service Calling createTrip ==> ");

        Trip trip = tripRepository.findByUserIdAndName(getCurrentLoggedInUser().getId(), name);

        if (trip != null) {
            throw new InformationExistException(
                    "trip with name " + trip.getName() + " already exists"
            );
        }

        Trip newTrip = new Trip();

        newTrip.setName(name);
        newTrip.setDestination(destination);
        newTrip.setDescription(description);
        newTrip.setStartDate(startDate);
        newTrip.setEndDate(endDate);

        // The cover photo is optional
        if (image != null && !image.isEmpty()) {
            newTrip.setImageUrl(saveImage(image));
        }

        newTrip.setUser(getCurrentLoggedInUser());
        return tripRepository.save(newTrip);
    }

    private String saveImage(MultipartFile image) {
        try {

            // Create uploads directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Get original filename
            String originalFileName = image.getOriginalFilename();

            // Generate unique ID
            String uniqueId = UUID.randomUUID().toString();

            // Create unique filename
            String fileName = uniqueId + "_" + originalFileName;

            // Create file path (absolute, so it doesn't land in Tomcat's temp folder)
            Path filePath = uploadPath.resolve(fileName).toAbsolutePath();

            // Save image
            image.transferTo(filePath);

            // Path we store in the database
            return UPLOAD_DIR + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Could not save image", e);
        }
    }

    // Only the trips of the logged-in user
    public List<Trip> getTrips() {
        System.out.println("Service calling getTrips ==>");
        return tripRepository.findByUserId(getCurrentLoggedInUser().getId());
    }

    public Trip getTrip(Long tripId) {
        System.out.println("service getTrip ==>");

        return tripRepository.findByIdAndUserId(tripId, getCurrentLoggedInUser().getId())
                .orElseThrow(() ->
                        new InformationNotFoundException(
                                "trip with id " + tripId + " not found"
                        )
                );
    }

    public Trip updateTrip(Long tripId, Trip tripObject) {
        System.out.println("service calling updateTrip ==>");

        Trip existingTrip = getTrip(tripId);

        existingTrip.setName(tripObject.getName());
        existingTrip.setDestination(tripObject.getDestination());
        existingTrip.setDescription(tripObject.getDescription());
        existingTrip.setStartDate(tripObject.getStartDate());
        existingTrip.setEndDate(tripObject.getEndDate());

        return tripRepository.save(existingTrip);
    }

    public Trip deleteTrip(Long tripId) {
        System.out.println("service calling deleteTrip ==>");

        Trip trip = getTrip(tripId);

        tripRepository.delete(trip);
        return trip;
    }
}
