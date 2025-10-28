package com.flightaggregator.flight_aggregator_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightaggregator.flight_aggregator_api.dto.FlightResponse;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchRequest;
import com.flightaggregator.flight_aggregator_api.service.FlightAggregatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightSearchController {
  // TODO: Add global error handling

  @Autowired
  private FlightAggregatorService flightAggregatorService;

  @GetMapping("/{origin}/{destination}/{departureDate}")
  public ResponseEntity<List<FlightResponse>> getAllFlights(
      @PathVariable String origin,
      @PathVariable String destination,
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDate) {

    FlightSearchRequest request = new FlightSearchRequest(origin, destination, departureDate);

    List<FlightResponse> allFlights = flightAggregatorService.searchFlightsFromProviderA(request);
    allFlights.addAll(flightAggregatorService.searchFlightsFromProviderB(request));
    return ResponseEntity.ok(allFlights);
  }

  @GetMapping("/cheapest/{origin}/{destination}/{departureDate}")
  public ResponseEntity<List<FlightResponse>> getCheapestFlights(
      @PathVariable String origin,
      @PathVariable String destination,
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDate) {

    FlightSearchRequest request = new FlightSearchRequest(origin, destination, departureDate);
    List<FlightResponse> cheapestFlights = flightAggregatorService.getCheapestFlights(request);

    return ResponseEntity.ok(cheapestFlights);
  }

  @GetMapping("/provider-a")
  public ResponseEntity<List<FlightResponse>> searchFlightsFromProviderA(
      @RequestParam String origin,
      @RequestParam String destionation,
      @RequestParam String departureDate) {
    FlightSearchRequest request = new FlightSearchRequest(origin, destionation, LocalDateTime.parse(departureDate));
    return ResponseEntity.ok(flightAggregatorService.searchFlightsFromProviderA(request));
  }

  @GetMapping("/provider-b")
  public ResponseEntity<List<FlightResponse>> searchFlightsFromProviderB(
      @RequestParam String departure,
      @RequestParam String arrival,
      @RequestParam String departureDate) {
    FlightSearchRequest request = new FlightSearchRequest(departure, arrival, LocalDateTime.parse(departureDate));
    return ResponseEntity.ok(flightAggregatorService.searchFlightsFromProviderB(request));
  }

}
