package com.flightaggregator.flight_aggregator_api.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flightaggregator.flight_aggregator_api.entity.ApiLog;
import com.flightaggregator.flight_aggregator_api.service.ApiLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @GetMapping("/provider-a")
  @Operation(summary = "Get logs with Provider A flights", description = "Get logs where Provider A returned results")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved logs"),
      @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
  })
  public ResponseEntity<Page<ApiLog>> getLogsWithProviderA(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDirection) {

    Pageable pageable = apiLogService.createDefaultPageable(page, size, sortBy, sortDirection);
    return ResponseEntity.ok(apiLogService.getLogsWithProviderA(pageable));
  }

  @GetMapping("/provider-b")
  @Operation(summary = "Get logs with Provider B flights", description = "Get logs where Provider B returned results")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved logs"),
      @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
  })
  public ResponseEntity<Page<ApiLog>> getLogsWithProviderB(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDirection) {

    Pageable pageable = apiLogService.createDefaultPageable(page, size, sortBy, sortDirection);
    return ResponseEntity.ok(apiLogService.getLogsWithProviderB(pageable));
  }

  @GetMapping("/route/{origin}/{destination}")
  @Operation(summary = "Get logs by route", description = "Get logs for a specific origin-destination pair")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved logs"),
      @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
  })
  public ResponseEntity<Page<ApiLog>> getLogsByRoute(
      @PathVariable String origin,
      @PathVariable String destination,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDirection) {

    Pageable pageable = apiLogService.createDefaultPageable(page, size, sortBy, sortDirection);
    return ResponseEntity.ok(apiLogService.getLogsByRoute(origin, destination, pageable));
  }

  @GetMapping("/date/{date}")
  @Operation(summary = "Get logs by departure date", description = "Get logs for a specific departure date")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved logs"),
      @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
  })
  public ResponseEntity<Page<ApiLog>> getLogsByDate(
      @Parameter(description = "Departure date", example = "2026-01-01") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDirection) {

    Pageable pageable = apiLogService.createDefaultPageable(page, size, sortBy, sortDirection);
    return ResponseEntity.ok(apiLogService.getLogsByDepartureDate(date, pageable));
  }
}
