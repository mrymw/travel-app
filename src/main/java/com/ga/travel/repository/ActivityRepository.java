package com.ga.travel.repository;

import com.ga.travel.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByTripIdOrderByDayAscTimeAsc(Long tripId);
    Optional<Activity> findByIdAndTripId(Long id, Long tripId);
}
