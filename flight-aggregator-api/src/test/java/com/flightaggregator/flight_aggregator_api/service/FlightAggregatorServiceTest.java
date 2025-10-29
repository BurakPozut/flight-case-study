package com.flightaggregator.flight_aggregator_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flightaggregator.flight_aggregator_api.dto.FlightResponse;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchRequest;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchResult;
import com.flightaggregator.flight_aggregator_api.model.providerA.FlightA;
import com.flightaggregator.flight_aggregator_api.model.providerA.SearchRequestA;
import com.flightaggregator.flight_aggregator_api.model.providerA.SearchResultA;
import com.flightaggregator.flight_aggregator_api.model.providerB.FlightB;
import com.flightaggregator.flight_aggregator_api.model.providerB.SearchRequestB;
import com.flightaggregator.flight_aggregator_api.model.providerB.SearchResultB;

@ExtendWith(MockitoExtension.class)
public class FlightAggregatorServiceTest {

  @Mock
  private FlightProviderAClient providerAClient;

  @Mock
  private FlightProviderBClient providerBClient;

  @InjectMocks
  private FlightAggregatorService flightAggregatorService;

  private FlightSearchRequest request;
  private LocalDateTime deparrtureDateTime;

  @BeforeEach
  void setup() {
    deparrtureDateTime = LocalDateTime.of(2026, 12, 25, 10, 0, 0);
    request = new FlightSearchRequest("IST", "COV", deparrtureDateTime);
  }

  @Test
  void testSearchFlightProviderA_Success() {
    // Arrange. In here we are creating what will our method get instead of calling
    // the actual soap provider
    FlightA flightA = getFlightA();

    SearchResultA searchResultA = new SearchResultA();
    searchResultA.setHasError(false);
    searchResultA.setFlightOptions(List.of(flightA));

    CompletableFuture<SearchResultA> future = CompletableFuture.completedFuture(searchResultA);
    when(providerAClient.callAvailabilitySearch(any(SearchRequestA.class))).thenReturn(future);

    // Assert
    FlightSearchResult result = flightAggregatorService.searchFlightsFromProviderA(request);

    assertNotNull(result);
    assertEquals(1, result.getFlights().size());
    FlightResponse flightResponse = result.getFlights().get(0);
    assertEquals("TK001", flightResponse.getFlightNo());
    assertEquals("IST", flightResponse.getOrigin());
    assertEquals("COV", flightResponse.getDestination());
    assertEquals("FlightProviderA", flightResponse.getProvider());
    assertEquals(1, result.getMetadata().getProviderACount());
    assertNotNull(result.getMetadata().getProviderALatencyMs());

    verify(providerAClient).callAvailabilitySearch(any(SearchRequestA.class));

  }

  @Test
  void testSearchFlightsFromProviderA_WithError() {
    SearchResultA searchResultA = new SearchResultA();
    searchResultA.setHasError(true);
    searchResultA.setErrorMessage("Provider Error");

    CompletableFuture<SearchResultA> future = CompletableFuture.completedFuture(searchResultA);
    when(providerAClient.callAvailabilitySearch(any(SearchRequestA.class))).thenReturn(future);

    FlightSearchResult result = flightAggregatorService.searchFlightsFromProviderA(request);

    assertNotNull(result);
    assertEquals(0, result.getFlights().size());
    assertEquals(0, result.getMetadata().getProviderACount());
  }

  @Test
  void testSearchFlightsFromProviderA_Timeout() {
    CompletableFuture<SearchResultA> future = new CompletableFuture<>();
    future.completeExceptionally(new TimeoutException("Timeout"));

    when(providerAClient.callAvailabilitySearch(any(SearchRequestA.class))).thenReturn(future);

    FlightSearchResult result = flightAggregatorService.searchFlightsFromProviderA(request);

    assertNotNull(result);
    assertEquals(0, result.getFlights().size());
    assertNotNull(result.getMetadata().getProviderALatencyMs());
  }

  @Test
  void testSearchFlightsFromProviderB_Success() {
    FlightB flightB = getFlightB();

    SearchResultB resultB = new SearchResultB();
    resultB.setHasError(false);
    resultB.setFlightOptions(List.of(flightB));

    CompletableFuture<SearchResultB> future = CompletableFuture.completedFuture(resultB);
    when(providerBClient.callAvailabilitySearch(any(SearchRequestB.class))).thenReturn(future);

    FlightSearchResult result = flightAggregatorService.searchFlightsFromProviderB(request);

    assertNotNull(result);
    assertEquals(1, result.getFlights().size());
    FlightResponse flightResponse = result.getFlights().get(0);
    assertEquals("BA001", flightResponse.getFlightNo());
    assertEquals("IST", flightResponse.getOrigin());
    assertEquals("COV", flightResponse.getDestination());
    assertEquals("FlightProviderB", flightResponse.getProvider());
    assertEquals(1, result.getMetadata().getProviderBCount());
    assertNotNull(result.getMetadata().getProviderBLatencyMs());

    verify(providerBClient).callAvailabilitySearch(any(SearchRequestB.class));
  }
  // We don't need for the rest for the provider b since this is a case study you
  // get the picture

  @Test
  void testGetAllFlights_Success() {
    FlightA flightA = getFlightA();
    FlightB flightB = getFlightB();

    SearchResultA searchResultA = new SearchResultA();
    searchResultA.setHasError(false);
    searchResultA.setFlightOptions(List.of(flightA));

    SearchResultB searchResultB = new SearchResultB();
    searchResultB.setHasError(false);
    searchResultB.setFlightOptions(List.of(flightB));

    CompletableFuture<SearchResultA> futureA = CompletableFuture.completedFuture(searchResultA);
    CompletableFuture<SearchResultB> futureB = CompletableFuture.completedFuture(searchResultB);

    when(providerAClient.callAvailabilitySearch(any(SearchRequestA.class))).thenReturn(futureA);
    when(providerBClient.callAvailabilitySearch(any(SearchRequestB.class))).thenReturn(futureB);

    // Act
    FlightSearchResult result = flightAggregatorService.getAllFlights(request);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.getFlights().size());
    assertEquals(1, result.getMetadata().getProviderACount());
    assertEquals(1, result.getMetadata().getProviderBCount());
    assertEquals(2, result.getMetadata().getTotalFlights());
  }

  void testGetCheapestFlights_Success() {
    FlightA flightA = getFlightA();
    FlightB flightB = getFlightB();

    SearchResultA searchResultA = new SearchResultA();
    searchResultA.setHasError(false);
    searchResultA.setFlightOptions(List.of(flightA));

    SearchResultB searchResultB = new SearchResultB();
    searchResultB.setHasError(false);
    searchResultB.setFlightOptions(List.of(flightB));

    CompletableFuture<SearchResultA> futureA = CompletableFuture.completedFuture(searchResultA);
    CompletableFuture<SearchResultB> futureB = CompletableFuture.completedFuture(searchResultB);

    when(providerAClient.callAvailabilitySearch(any(SearchRequestA.class))).thenReturn(futureA);
    when(providerBClient.callAvailabilitySearch(any(SearchRequestB.class))).thenReturn(futureB);

    FlightSearchResult result = flightAggregatorService.getCheapestFlights(request);

    assertNotNull(result);
    assertEquals(1, result.getFlights().size());
    assertEquals(new BigDecimal("279.99"), result.getFlights().get(0).getPrice());
    assertEquals("FlightProviderB", result.getFlights().get(0).getProvider());
  }

  @Test
  void testGetCheapestFlights_DifferentFlights() throws Exception {
    // Arrange - different flights
    FlightA flightA = getFlightA();
    FlightB flightB = getFlightB();

    SearchResultA searchResultA = new SearchResultA();
    searchResultA.setHasError(false);
    searchResultA.setFlightOptions(List.of(flightA));

    SearchResultB searchResultB = new SearchResultB();
    searchResultB.setHasError(false);
    searchResultB.setFlightOptions(List.of(flightB));

    CompletableFuture<SearchResultA> futureA = CompletableFuture.completedFuture(searchResultA);
    CompletableFuture<SearchResultB> futureB = CompletableFuture.completedFuture(searchResultB);

    when(providerAClient.callAvailabilitySearch(any(SearchRequestA.class))).thenReturn(futureA);
    when(providerBClient.callAvailabilitySearch(any(SearchRequestB.class))).thenReturn(futureB);

    // Act
    FlightSearchResult result = flightAggregatorService.getCheapestFlights(request);

    // Assert - should have both flights
    assertNotNull(result);
    assertEquals(2, result.getFlights().size());
    // Should be sorted by price
    assertTrue(result.getFlights().get(0).getPrice().compareTo(result.getFlights().get(1).getPrice()) <= 0);
  }

  private FlightA getFlightA() {
    return createFlightA("TK001", "IST", "COV", deparrtureDateTime,
        deparrtureDateTime.plusHours(4), new BigDecimal("299.99"));
  }

  private FlightA createFlightA(String flightNo, String origin, String destination, LocalDateTime departure,
      LocalDateTime arrival, BigDecimal price) {
    FlightA flightA = new FlightA();
    flightA.setFlightNo(flightNo);
    flightA.setOrigin(origin);
    flightA.setDestination(destination);
    flightA.setDeparturedatetime(departure);
    flightA.setArrivaldatetime(arrival);
    flightA.setPrice(price);
    return flightA;
  }

  private FlightB getFlightB() {
    return createFlightB("BA001", "IST", "COV", deparrtureDateTime,
        deparrtureDateTime.plusHours(5), new BigDecimal("349.99"));
  }

  private FlightB createFlightB(String flightNumber, String departure, String arrival, LocalDateTime departureDateTime,
      LocalDateTime arrivalDateTime, BigDecimal price) {
    FlightB flightB = new FlightB();
    flightB.setFlightNumber(flightNumber);
    flightB.setDeparture(departure);
    flightB.setDestination(arrival);
    flightB.setDeparturedatetime(departureDateTime);
    flightB.setArrivaldatetime(arrivalDateTime);
    flightB.setPrice(price);
    return flightB;
  }
}
