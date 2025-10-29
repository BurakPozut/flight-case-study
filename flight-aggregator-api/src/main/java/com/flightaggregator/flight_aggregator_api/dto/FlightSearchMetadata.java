package com.flightaggregator.flight_aggregator_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class FlightSearchMetadata {
  private String endpoint;
  private String origin;
  private String destination;
  private LocalDate departureDate;
  private Integer providerALatencyMs;
  private Integer providerBLatencyMs;
  private Integer providerACount;
  private Integer providerBCount;
  private BigDecimal minPrice;
  private BigDecimal maxPrice;
  private Integer totalFlights;

  public FlightSearchMetadata() {
  }

  public FlightSearchMetadata(String endpoint) {
    this.endpoint = endpoint;
  }

  public void updateWithFlightData(List<FlightResponse> flights) {
    this.totalFlights = flights.size();

    if (!flights.isEmpty()) {
      List<BigDecimal> prices = flights.stream()
          .map(FlightResponse::getPrice)
          .collect(Collectors.toList());

      this.minPrice = prices.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
      this.maxPrice = prices.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

      this.providerACount = (int) flights.stream()
          .filter(f -> "FlightProviderA".equals(f.getProvider()))
          .count();

      this.providerBCount = (int) flights.stream()
          .filter(f -> "FlightProviderB".equals(f.getProvider()))
          .count();
    }
  }

  // Getters and Setters
  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
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

  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDate departureDate) {
    this.departureDate = departureDate;
  }

  public Integer getProviderALatencyMs() {
    return providerALatencyMs;
  }

  public void setProviderALatencyMs(Integer providerALatencyMs) {
    this.providerALatencyMs = providerALatencyMs;
  }

  public Integer getProviderBLatencyMs() {
    return providerBLatencyMs;
  }

  public void setProviderBLatencyMs(Integer providerBLatencyMs) {
    this.providerBLatencyMs = providerBLatencyMs;
  }

  public Integer getProviderACount() {
    return providerACount;
  }

  public void setProviderACount(Integer providerACount) {
    this.providerACount = providerACount;
  }

  public Integer getProviderBCount() {
    return providerBCount;
  }

  public void setProviderBCount(Integer providerBCount) {
    this.providerBCount = providerBCount;
  }

  public BigDecimal getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(BigDecimal minPrice) {
    this.minPrice = minPrice;
  }

  public BigDecimal getMaxPrice() {
    return maxPrice;
  }

  public void setMaxPrice(BigDecimal maxPrice) {
    this.maxPrice = maxPrice;
  }

  public Integer getTotalFlights() {
    return totalFlights;
  }

  public void setTotalFlights(Integer totalFlights) {
    this.totalFlights = totalFlights;
  }
}