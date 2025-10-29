package com.flightaggregator.flight_aggregator_api.respository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.flightaggregator.flight_aggregator_api.entity.ApiLog;
import java.time.LocalDateTime;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {

  Page<ApiLog> findByEndpointContaining(String endpoint, Pageable pageable);

  Page<ApiLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

  Page<ApiLog> findByProviderACountGreaterThan(Integer count, Pageable pageable);

  Page<ApiLog> findByProviderBCountGreaterThan(Integer count, Pageable pageable);

  Page<ApiLog> findByOriginAndDestination(String origin, String destination, Pageable pageable);

  Page<ApiLog> findByDepartureDate(java.time.LocalDate departureDate, Pageable pageable);

  Page<ApiLog> findByDurationMsGreaterThan(Integer threshold, Pageable pageable);

  @Query("SELECT DATE_TRUNC('hour', a.createdAt) as hour, COUNT(a) FROM ApiLog a GROUP BY hour ORDER BY hour DESC")
  List<Object[]> getRequestCountByHour();

}
