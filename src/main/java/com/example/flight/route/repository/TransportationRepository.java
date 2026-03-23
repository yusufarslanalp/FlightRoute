package com.example.flight.route.repository;




import com.example.flight.route.model.Location;
import com.example.flight.route.model.Transportation;
import com.example.flight.route.model.TransportationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransportationRepository extends JpaRepository<Transportation, Long> {
    Optional<Transportation> findById(Long id);

    List<Transportation> findByFrom(Location from);

    List<Transportation> findByTo(Location from);

    List<Transportation> findByFromAndToAndType(Location from, Location to, TransportationType type);

}
