package com.ga.travel.repository;

import com.ga.travel.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    Trip findByUserIdAndName(Long userId, String tripName);
    List<Trip> findByUserId(Long userId);
    Optional<Trip> findByIdAndUserId(Long tripId, Long userId);
}
