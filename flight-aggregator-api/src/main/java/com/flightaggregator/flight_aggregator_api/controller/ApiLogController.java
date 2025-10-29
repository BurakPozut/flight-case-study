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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/logs")
@Tag(name = "API Logs", description = "API logging and monitoring endpoints")
public class ApiLogController {
  @Autowired
  private ApiLogService apiLogService;

  @GetMapping
  @Operation(summary = "Get All API logs", description = "Retrieve paginated list of all API logs with sorting options")
  @ApiResponse(responseCode = "200", description = "Seuccessfully retrieved API logs", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiLog.class)))
  public ResponseEntity<Page<ApiLog>> getAllLogs(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDirection) {

    Pageable pageable = apiLogService.createDefaultPageable(page, size, sortBy, sortDirection);
    return ResponseEntity.ok(apiLogService.getAllLogs(pageable));
  }

  @GetMapping("/provider/{provider}") // TODO: This doesnt work
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
