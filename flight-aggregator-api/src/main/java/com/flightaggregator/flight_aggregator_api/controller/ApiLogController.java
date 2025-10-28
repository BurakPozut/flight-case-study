package com.flightaggregator.flight_aggregator_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flightaggregator.flight_aggregator_api.entity.ApiLog;
import com.flightaggregator.flight_aggregator_api.service.ApiLogService;

@RestController
@RequestMapping("/api/v1/logs")
public class ApiLogController {
  @Autowired
  private ApiLogService apiLogService;

  @GetMapping
  public ResponseEntity<Page<ApiLog>> getAllLogs(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDirection) {

    Pageable pageable = apiLogService.createDefaultPageable(page, size, sortBy, sortDirection);
    return ResponseEntity.ok(apiLogService.getAllLogs(pageable));
  }

  @GetMapping("/provider/{provider}")
  public ResponseEntity<Page<ApiLog>> getLogsByProvider(
      @PathVariable String provider,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDirection) {

    Pageable pageable = apiLogService.createDefaultPageable(page, size, sortBy, sortDirection);
    return ResponseEntity.ok(apiLogService.getLogsByProvider(provider, pageable));
  }

}
