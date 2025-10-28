package com.flightaggregator.flight_aggregator_api.entity;

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

  @Column(name = "request_method", length = 255)
  private String requestMethod;
  @Column(name = "request_data", columnDefinition = "TEXT")
  private String requestData;
  @Column(name = "response_data", columnDefinition = "TEXT")
  private String responseData;

  @Column(name = "response_time_ms")
  private Integer responseTimeMs;

  @Column(name = "provider", length = 50)
  private String provider;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  // Constructors
  public ApiLog() {
    this.createdAt = LocalDateTime.now();
  }

  public ApiLog(String endpoint, String requestMethod, String requestData,
      String responseData, Integer responseTimeMs, String provider) {
    this();
    this.endpoint = endpoint;
    this.requestMethod = requestMethod;
    this.requestData = requestData;
    this.responseData = responseData;
    this.responseTimeMs = responseTimeMs;
    this.provider = provider;
  }

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

  public String getRequestMethod() {
    return requestMethod;
  }

  public void setRequestMethod(String requestMethod) {
    this.requestMethod = requestMethod;
  }

  public String getRequestData() {
    return requestData;
  }

  public void setRequestData(String requestData) {
    this.requestData = requestData;
  }

  public String getResponseData() {
    return responseData;
  }

  public void setResponseData(String responseData) {
    this.responseData = responseData;
  }

  public Integer getResponseTimeMs() {
    return responseTimeMs;
  }

  public void setResponseTimeMs(Integer responseTimeMs) {
    this.responseTimeMs = responseTimeMs;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "ApiLog{" +
        "id=" + id +
        ", endpoint='" + endpoint + '\'' +
        ", requestMethod='" + requestMethod + '\'' +
        ", responseTimeMs=" + responseTimeMs +
        ", provider='" + provider + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}
