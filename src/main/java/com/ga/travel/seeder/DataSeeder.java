package com.ga.travel.seed;

import com.ga.travel.model.Activity;
import com.ga.travel.model.Trip;
import com.ga.travel.model.User;
import com.ga.travel.model.UserProfile;
import com.ga.travel.repository.ActivityRepository;
import com.ga.travel.repository.TripRepository;
import com.ga.travel.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Fills the database with sample data when the app starts.
 * Only runs when the users table is empty, so restarting won't create duplicates.
 * Every seeded user logs in with the password: password123
 */
@Component
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final String PASSWORD = "password123";

    private UserRepository userRepository;
    private TripRepository tripRepository;
    private ActivityRepository activityRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            System.out.println("Seed data already exists, skipping ==>");
            return;
        }
        System.out.println("Seeding sample data ==>");

        // ---------- Sara ----------
        User sara = user("sara", "sara@mail.com", "Sara", "Ahmed", "Loves food and big cities.");

        Trip tokyo = trip(sara, "Winter in Tokyo", "Tokyo, Japan",
                "A week of ramen, temples and city lights.",
                LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 27));
        activity(sara, tokyo, "Visit Senso-ji Temple", 1, "09:30", "Asakusa", 0.0, "Go early to beat the crowds", true);
        activity(sara, tokyo, "Ramen lunch at Ichiran", 1, "13:00", "Shibuya", 12.0, "Solo booths, order with the paper form", false);
        activity(sara, tokyo, "Shibuya Sky at sunset", 1, "16:30", "Shibuya Scramble Square", 25.0, "Book tickets online", true);
        activity(sara, tokyo, "TeamLab Planets", 2, "10:00", "Toyosu", 30.0, "Wear shorts, you walk through water", true);
        activity(sara, tokyo, "Day trip to Mount Fuji", 3, "07:00", "Kawaguchiko", 60.0, "Bus from Shinjuku", false);
        activity(sara, tokyo, "Tsukiji Outer Market breakfast", 4, "08:00", "Tsukiji", 20.0, "Try the tamagoyaki", false);

        Trip dubai = trip(sara, "Weekend in Dubai", "Dubai, UAE",
                "A quick weekend getaway.",
                LocalDate.of(2027, 1, 15), LocalDate.of(2027, 1, 17));
        activity(sara, dubai, "Dubai Mall and the fountain show", 1, "18:00", "Downtown Dubai", 0.0, "Shows every 30 minutes", false);
        activity(sara, dubai, "Desert safari", 2, "15:00", "Lahbab Desert", 70.0, "Pickup from the hotel", true);
        activity(sara, dubai, "Brunch at the marina", 3, "11:00", "Dubai Marina", 45.0, null, false);

        // ---------- Ali ----------
        User ali = user("ali", "ali@mail.com", "Ali", "Hassan", "Hiker and street-food hunter.");

        Trip istanbul = trip(ali, "Istanbul Food Tour", "Istanbul, Turkey",
                "Five days of eating our way across two continents.",
                LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 15));
        activity(ali, istanbul, "Hagia Sophia", 1, "09:00", "Sultanahmet", 25.0, "Cover shoulders and knees", true);
        activity(ali, istanbul, "Grand Bazaar", 1, "14:00", "Beyazit", 0.0, "Bargain politely", false);
        activity(ali, istanbul, "Bosphorus cruise", 2, "11:00", "Eminonu pier", 15.0, "Sit on the left side", true);
        activity(ali, istanbul, "Balik ekmek by the Galata Bridge", 2, "13:30", "Karakoy", 5.0, "Fish sandwich from the boats", false);
        activity(ali, istanbul, "Kadikoy market food walk", 3, "10:00", "Kadikoy", 30.0, "Ferry to the Asian side", false);

        Trip oman = trip(ali, "Hiking in Oman", "Jebel Shams, Oman",
                "Canyons, mountain villages and a night under the stars.",
                LocalDate.of(2027, 2, 4), LocalDate.of(2027, 2, 7));
        activity(ali, oman, "Balcony Walk (W6)", 1, "07:30", "Al Khitaim", 0.0, "Bring 3 litres of water", false);
        activity(ali, oman, "Camp night at the canyon rim", 1, "18:00", "Jebel Shams", 40.0, "It gets cold, pack a jacket", true);
        activity(ali, oman, "Nizwa Fort and souq", 2, "09:00", "Nizwa", 8.0, "Friday goat market is early", false);
        activity(ali, oman, "Wadi Ghul viewpoint", 3, "08:00", "Wadi Ghul", 0.0, null, false);

        // ---------- Noor ----------
        User noor = user("noor", "noor@mail.com", "Noor", "Khalid", "Planning a family holiday.");

        Trip paris = trip(noor, "Paris with Family", "Paris, France",
                "Spring holiday with the kids.",
                LocalDate.of(2027, 4, 1), LocalDate.of(2027, 4, 6));
        activity(noor, paris, "Eiffel Tower summit", 1, "10:00", "Champ de Mars", 35.0, "Tickets sell out, book ahead", true);
        activity(noor, paris, "Seine river cruise", 1, "19:00", "Port de la Bourdonnais", 18.0, null, true);
        activity(noor, paris, "Louvre Museum", 2, "09:00", "Rue de Rivoli", 22.0, "Enter through the Carrousel entrance", true);
        activity(noor, paris, "Disneyland Paris", 3, "08:30", "Marne-la-Vallee", 95.0, "RER A train from the city", false);
        activity(noor, paris, "Crepes in Montmartre", 4, "16:00", "Montmartre", 10.0, "Then sunset at Sacre-Coeur", false);

        System.out.println("Seeded 3 users, 5 trips and " + activityRepository.count() + " activities ==>");
    }

    private User user(String username, String email, String firstName, String lastName, String description) {
        UserProfile profile = new UserProfile();
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setProfileDescription(description);

        User user = new User();
        user.setUsername(username);
        user.setEmailAddress(email);
        user.setPassword(passwordEncoder.encode(PASSWORD)); // hashed, so login works
        user.setUserProfile(profile); // saved together thanks to CascadeType.ALL
        return userRepository.save(user);
    }

    private Trip trip(User owner, String name, String destination, String description,
                      LocalDate startDate, LocalDate endDate) {
        Trip trip = new Trip();
        trip.setName(name);
        trip.setDestination(destination);
        trip.setDescription(description);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        trip.setUser(owner);
        return tripRepository.save(trip);
    }

    private void activity(User owner, Trip trip, String name, int day, String time,
                          String location, Double cost, String notes, boolean booked) {
        Activity activity = new Activity();
        activity.setName(name);
        activity.setDay(day);
        activity.setTime(time);
        activity.setLocation(location);
        activity.setCost(cost);
        activity.setNotes(notes);
        activity.setBooked(booked);
        activity.setTrip(trip);
        activity.setUser(owner);
        activityRepository.save(activity);
    }
}