package com.flightaggregator.flight_aggregator_api.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flightaggregator.flight_aggregator_api.dto.FlightSearchMetadata;
import com.flightaggregator.flight_aggregator_api.entity.ApiLog;
import com.flightaggregator.flight_aggregator_api.respository.ApiLogRepository;

@Service
public class ApiLogService {

  @Autowired
  private ApiLogRepository apiLogRepository;

  public ApiLog saveApiLog(FlightSearchMetadata metadata, Integer statusCode, Integer durationMs) {
    ApiLog apiLog = new ApiLog(
        metadata.getEndpoint(),
        "GET", // or extract from request
        statusCode,
        durationMs,
        metadata.getOrigin(),
        metadata.getDestination(),
        metadata.getDepartureDate(),
        metadata.getProviderALatencyMs(),
        metadata.getProviderBLatencyMs(),
        metadata.getProviderACount(),
        metadata.getProviderBCount(),
        metadata.getMinPrice(),
        metadata.getMaxPrice(),
        metadata.getTotalFlights());
    return apiLogRepository.save(apiLog);
  }

  public Page<ApiLog> getAllLogs(Pageable pageable) {
    return apiLogRepository.findAll(pageable);
  }

  public Page<ApiLog> getLogsByEndpoint(String endpoint, Pageable pageable) {
    return apiLogRepository.findByEndpointContaining(endpoint, pageable);
  }

  public Page<ApiLog> getLogsByTimeRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
    return apiLogRepository.findByCreatedAtBetween(start, end, pageable);
  }

  public Page<ApiLog> getLogsWithProviderA(Pageable pageable) {
    return apiLogRepository.findByProviderACountGreaterThan(0, pageable);
  }

  public Page<ApiLog> getLogsWithProviderB(Pageable pageable) {
    return apiLogRepository.findByProviderBCountGreaterThan(0, pageable);
  }

  public Page<ApiLog> getLogsByRoute(String origin, String destination, Pageable pageable) {
    return apiLogRepository.findByOriginAndDestination(origin, destination, pageable);
  }

  public Page<ApiLog> getLogsByDepartureDate(java.time.LocalDate departureDate, Pageable pageable) {
    return apiLogRepository.findByDepartureDate(departureDate, pageable);
  }

  public Page<ApiLog> getLogsWithSlowResponse(Integer thresholdMs, Pageable pageable) {
    return apiLogRepository.findByDurationMsGreaterThan(thresholdMs, pageable);
  }

  public Pageable createDefaultPageable(int page, int size, String sortBy, String sortDirection) {
    Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    return PageRequest.of(page, size, sort);
  }
}
