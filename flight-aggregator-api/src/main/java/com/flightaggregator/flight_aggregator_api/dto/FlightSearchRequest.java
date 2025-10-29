package com.flightaggregator.flight_aggregator_api.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Filgth search request")
public class FlightSearchRequest {
  @Schema(description = "Origin airport code", example = "IST", requiredMode = RequiredMode.REQUIRED)
  private String origin;

  @Schema(description = "Destination airport code", example = "LHR", required = true)
  private String destination;

  @Schema(description = "Departure date and time", example = "2024-12-25T10:00:00", required = true)
  private LocalDateTime departureDate;

  public FlightSearchRequest() {
  }

  public FlightSearchRequest(String origin, String destination, LocalDateTime departureDate) {
    this.origin = origin;
    this.destination = destination;
    this.departureDate = departureDate;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public LocalDateTime getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDateTime departureDate) {
    this.departureDate = departureDate;
  }
}
