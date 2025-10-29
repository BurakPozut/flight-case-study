package com.flightaggregator.flight_aggregator_api.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "api_logs")
public class ApiLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "endpoint", length = 255)
  private String endpoint;

  @Column(name = "method", length = 10)
  private String method;

  @Column(name = "status")
  private Integer status;

  @Column(name = "duration_ms")
  private Integer durationMs;

  @Column(name = "origin", length = 10)
  private String origin;

  @Column(name = "destination", length = 10)
  private String destination;

  @Column(name = "departure_date")
  private LocalDate departureDate;

  @Column(name = "provider_a_latency_ms")
  private Integer providerALatencyMs;

  @Column(name = "provider_b_latency_ms")
  private Integer providerBLatencyMs;

  @Column(name = "provider_a_count")
  private Integer providerACount;

  @Column(name = "provider_b_count")
  private Integer providerBCount;

  @Column(name = "min_price", precision = 10, scale = 2)
  private BigDecimal minPrice;

  @Column(name = "max_price", precision = 10, scale = 2)
  private BigDecimal maxPrice;

  @Column(name = "total_flights")
  private Integer totalFlights;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  // Constructors
  public ApiLog() {
    this.createdAt = LocalDateTime.now();
  }

  public ApiLog(String endpoint, String method, Integer status, Integer durationMs,
      String origin, String destination, LocalDate departureDate,
      Integer providerALatencyMs, Integer providerBLatencyMs,
      Integer providerACount, Integer providerBCount,
      BigDecimal minPrice, BigDecimal maxPrice, Integer totalFlights) {
    this();
    this.endpoint = endpoint;
    this.method = method;
    this.status = status;
    this.durationMs = durationMs;
    this.origin = origin;
    this.destination = destination;
    this.departureDate = departureDate;
    this.providerALatencyMs = providerALatencyMs;
    this.providerBLatencyMs = providerBLatencyMs;
    this.providerACount = providerACount;
    this.providerBCount = providerBCount;
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
    this.totalFlights = totalFlights;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getDurationMs() {
    return durationMs;
  }

  public void setDurationMs(Integer durationMs) {
    this.durationMs = durationMs;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}