package com.flightaggregator.flight_aggregator_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightaggregator.flight_aggregator_api.dto.FlightResponse;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchRequest;
import com.flightaggregator.flight_aggregator_api.service.FlightAggregatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightSearchController {

  @Autowired
  private FlightAggregatorService flightAggregatorService;

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
    @RequestParam String departureDate
  ) {
    FlightSearchRequest request = new FlightSearchRequest(departure, arrival, LocalDateTime.parse(departureDate));
    return ResponseEntity.ok(flightAggregatorService.searchFlightsFromProviderB(request));
  }
  

}
