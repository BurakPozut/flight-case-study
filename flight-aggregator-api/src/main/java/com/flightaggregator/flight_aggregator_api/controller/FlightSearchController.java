package com.flightaggregator.flight_aggregator_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.flightaggregator.flight_aggregator_api.dto.FlightResponse;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchRequest;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchResult;
import com.flightaggregator.flight_aggregator_api.service.FlightAggregatorService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/flights")
@Tag(name = "Flight Search", description = "Flight search and aggregation endpoints")
public class FlightSearchController {

  @Autowired
  private FlightAggregatorService flightAggregatorService;

  @GetMapping("/{origin}/{destination}/{departureDate}")
  @ApiResponse(responseCode = "200", description = "Successfully retrived flight search results", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightResponse.class)))
  public ResponseEntity<List<FlightResponse>> getAllFlights(
      @PathVariable String origin,
      @PathVariable String destination,
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDate) {

    FlightSearchRequest request = new FlightSearchRequest(origin, destination, departureDate);
    FlightSearchResult result = flightAggregatorService.getAllFlights(request);

    // Store metadata in request scope for the aspect to access
    HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .getRequest();
    httpRequest.setAttribute("flightSearchMetadata", result.getMetadata());

    return ResponseEntity.ok(result.getFlights());
  }

  @GetMapping("/cheapest/{origin}/{destination}/{departureDate}")
  public ResponseEntity<List<FlightResponse>> getCheapestFlights(
      @PathVariable String origin,
      @PathVariable String destination,
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDate) {

    FlightSearchRequest request = new FlightSearchRequest(origin, destination, departureDate);
    FlightSearchResult result = flightAggregatorService.getCheapestFlights(request);

    HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .getRequest();
    httpRequest.setAttribute("flightSearchMetadata", result.getMetadata());

    return ResponseEntity.ok(result.getFlights());
  }

  @GetMapping("/provider-a")
  public ResponseEntity<List<FlightResponse>> searchFlightsFromProviderA(
      @RequestParam String origin,
      @RequestParam String destination,
      @RequestParam String departureDate) {

    FlightSearchRequest request = new FlightSearchRequest(origin, destination, LocalDateTime.parse(departureDate));
    FlightSearchResult result = flightAggregatorService.searchFlightsFromProviderA(request);

    // Store metadata in request scope for the aspect to access
    HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .getRequest();
    httpRequest.setAttribute("flightSearchMetadata", result.getMetadata());

    return ResponseEntity.ok(result.getFlights());
  }

  @GetMapping("/provider-b")
  public ResponseEntity<List<FlightResponse>> searchFlightsFromProviderB(
      @RequestParam String departure,
      @RequestParam String arrival,
      @RequestParam String departureDate) {

    FlightSearchRequest request = new FlightSearchRequest(departure, arrival, LocalDateTime.parse(departureDate));
    FlightSearchResult result = flightAggregatorService.searchFlightsFromProviderB(request);

    HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .getRequest();
    httpRequest.setAttribute("flightSearchMetadata", result.getMetadata());

    return ResponseEntity.ok(result.getFlights());
  }

}
