package com.flightaggregator.flight_aggregator_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FlightResponse {
  private String flightNo;
  private String origin;
  private String destination;
  private LocalDateTime departureDateTime;
  private LocalDateTime arrivalDateTime;
  private BigDecimal price;
  private String provider; // "FlightProviderA" or "FlightProviderB"

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
