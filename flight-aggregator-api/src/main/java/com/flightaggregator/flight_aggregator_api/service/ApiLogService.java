package com.flightaggregator.flight_aggregator_api.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flightaggregator.flight_aggregator_api.entity.ApiLog;
import com.flightaggregator.flight_aggregator_api.respository.ApiLogRepository;

@Service
public class ApiLogService {

  @Autowired
  private ApiLogRepository apiLogRepository;

  public ApiLog saveApiLog(String endpoint, String requestMethod, String requestData, String responseData,
      Integer responseTimeMs, String provider) {
    ApiLog apiLog = new ApiLog(endpoint, requestMethod, requestData, responseData, responseTimeMs, provider);
    return apiLogRepository.save(apiLog);
  }

  public Page<ApiLog> getAllLogs(Pageable pageable) {
    return apiLogRepository.findAll(pageable);
  }

  public Page<ApiLog> getLogsByProvider(String provider, Pageable pageable) {
    return apiLogRepository.findByProvider(provider, pageable);
  }

  public Page<ApiLog> getLogsByEndpoint(String endpoint, Pageable pageable) {
    return apiLogRepository.findByEndpointContaining(endpoint, pageable);
  }

  public Page<ApiLog> getLogsByTimeRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
    return apiLogRepository.findByCreatedAtBetween(start, end, pageable);
  }

  public Page<ApiLog> getLogsByProviderAndTimeRange(String provider, LocalDateTime start, LocalDateTime end,
      Pageable pageable) {
    return apiLogRepository.findByProviderAndCreatedAtBetween(provider, start, end, pageable);
  }

  public Pageable createDefaultPageable(int page, int size, String sortBy, String sortDirection) {
    Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    return PageRequest.of(page, size, sort);
  }
}
