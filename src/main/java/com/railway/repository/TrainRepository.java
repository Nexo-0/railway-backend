package com.railway.repository;

import com.railway.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    List<Train> findBySourceAndDestination(String source, String destination);

    @Query("SELECT t FROM Train t WHERE LOWER(t.source) LIKE LOWER(CONCAT('%', :source, '%')) " +
           "AND LOWER(t.destination) LIKE LOWER(CONCAT('%', :destination, '%'))")
    List<Train> searchTrains(@Param("source") String source, @Param("destination") String destination);

    List<Train> findByDepartureTimeAfter(LocalDateTime date);
}
