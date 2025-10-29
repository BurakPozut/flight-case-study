package com.flightaggregator.flight_aggregator_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Flight search response")
public class FlightResponse {
  @Schema(description = "Flight number", example = "TK001")
  private String flightNo;

  @Schema(description = "Origin airport code", example = "IST")
  private String origin;

  @Schema(description = "Destination airport code", example = "LHR")
  private String destination;

  @Schema(description = "Departure date and time", example = "2024-12-25T10:00:00")
  private LocalDateTime departureDateTime;

  @Schema(description = "Arrival date and time", example = "2024-12-25T14:30:00")
  private LocalDateTime arrivalDateTime;

  @Schema(description = "Flight price", example = "299.99")
  private BigDecimal price;

  @Schema(description = "Flight provider", example = "FlightProviderA", allowableValues = { "FlightProviderA",
      "FlightProviderB" })
  private String provider;

  public FlightResponse() {
  }

  public FlightResponse(String flightNo, String origin, String destination,
      LocalDateTime departureDateTime, LocalDateTime arrivalDateTime,
      BigDecimal price, String provider) {
    this.flightNo = flightNo;
    this.origin = origin;
    this.destination = destination;
    this.departureDateTime = departureDateTime;
    this.arrivalDateTime = arrivalDateTime;
    this.price = price;
    this.provider = provider;
  }

  public String getFlightNo() {
    return flightNo;
  }

  public void setFlightNo(String flightNo) {
    this.flightNo = flightNo;
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

  public LocalDateTime getDepartureDateTime() {
    return departureDateTime;
  }

  public void setDepartureDateTime(LocalDateTime departureDateTime) {
    this.departureDateTime = departureDateTime;
  }

  public LocalDateTime getArrivalDateTime() {
    return arrivalDateTime;
  }

  public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
    this.arrivalDateTime = arrivalDateTime;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }
}
