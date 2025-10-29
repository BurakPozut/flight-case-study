package com.flightaggregator.flight_aggregator_api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.flightaggregator.flight_aggregator_api.dto.FlightResponse;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchMetadata;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchRequest;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchResult;
import com.flightaggregator.flight_aggregator_api.service.FlightAggregatorService;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class FlightSearchControllerTest {

  @Mock
  private FlightAggregatorService flightAggregatorService;

  @Mock
  private HttpServletRequest httpServletRequest;

  @InjectMocks
  private FlightSearchController flightSearchController;

  private LocalDateTime departureDateTime;

  @BeforeEach
  void setup() {
    departureDateTime = LocalDateTime.of(2026, 12, 25, 10, 0);

    // Since we use aop and modify the request context we need to mock that as well
    ServletRequestAttributes requestAttributes = new ServletRequestAttributes(httpServletRequest);
    RequestContextHolder.setRequestAttributes(requestAttributes);
  }

  @Test
  void testGetAllFlights_Success() {
    FlightResponse flight = getFlight();

    FlightSearchMetadata metadata = new FlightSearchMetadata();
    metadata.setProviderACount(1);

    FlightSearchResult searchResult = new FlightSearchResult(List.of(flight), metadata);

    when(flightAggregatorService.getAllFlights(any(FlightSearchRequest.class))).thenReturn(searchResult);

    ResponseEntity<List<FlightResponse>> response = flightSearchController.getAllFlights("IST", "COV",
        departureDateTime);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    List<FlightResponse> body = Objects.requireNonNull(response.getBody());
    assertEquals(1, body.size());
    assertEquals("TK001", body.get(0).getFlightNo());

    verify(flightAggregatorService).getAllFlights(any(FlightSearchRequest.class));
    verify(httpServletRequest).setAttribute(eq("flightSearchMetadata"), eq(metadata));
  }

  @Test
  void testGetCheapestFlights_Success() {
    // Arrange
    FlightResponse flight = getFlight();

    FlightSearchMetadata metadata = new FlightSearchMetadata();
    metadata.setProviderACount(1);

    FlightSearchResult searchResult = new FlightSearchResult(List.of(flight), metadata);

    when(flightAggregatorService.getCheapestFlights(any(FlightSearchRequest.class)))
        .thenReturn(searchResult);

    // Act
    ResponseEntity<List<FlightResponse>> response = flightSearchController.getCheapestFlights("IST", "LHR",
        departureDateTime);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    List<FlightResponse> body = Objects.requireNonNull(response.getBody());
    assertEquals(1, body.size());

    verify(flightAggregatorService).getCheapestFlights(any(FlightSearchRequest.class));
  }

  @Test
  void testSearchFlightsFromProviderA_Success() {

    FlightResponse flight = getFlight();

    FlightSearchMetadata metadata = new FlightSearchMetadata();
    metadata.setProviderACount(1);

    FlightSearchResult searchResult = new FlightSearchResult(List.of(flight), metadata);

    when(flightAggregatorService.searchFlightsFromProviderA(any(FlightSearchRequest.class)))
        .thenReturn(searchResult);

    ResponseEntity<List<FlightResponse>> response = flightSearchController.searchFlightsFromProviderA("IST", "LHR",
        "2024-12-25T10:00:00");

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    List<FlightResponse> body = Objects.requireNonNull(response.getBody());
    assertEquals(1, body.size());

    verify(flightAggregatorService).searchFlightsFromProviderA(any(FlightSearchRequest.class));
  }

  @Test
  void testSearchFlightsFromProviderB_Success() {
    // Arrange
    FlightResponse flight = getFlight();

    FlightSearchMetadata metadata = new FlightSearchMetadata();
    metadata.setProviderBCount(1);

    FlightSearchResult searchResult = new FlightSearchResult(List.of(flight), metadata);

    when(flightAggregatorService.searchFlightsFromProviderB(any(FlightSearchRequest.class)))
        .thenReturn(searchResult);

    // Act
    ResponseEntity<List<FlightResponse>> response = flightSearchController.searchFlightsFromProviderB("IST", "COV",
        "2024-12-25T10:00:00");

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    List<FlightResponse> body = Objects.requireNonNull(response.getBody());
    assertEquals(1, body.size());

    verify(flightAggregatorService).searchFlightsFromProviderB(any(FlightSearchRequest.class));
  }

  private FlightResponse getFlight() {
    return new FlightResponse(
        "TK001", "IST", "LHR",
        departureDateTime, departureDateTime.plusHours(4),
        new BigDecimal("299.99"), "FlightProviderA");
  }
}
