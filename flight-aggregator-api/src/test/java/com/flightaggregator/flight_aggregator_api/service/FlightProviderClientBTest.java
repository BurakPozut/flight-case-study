package com.flightaggregator.flight_aggregator_api.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.flightaggregator.flight_aggregator_api.model.providerB.SearchRequestB;
import com.flightaggregator.flight_aggregator_api.model.providerB.SearchResultB;

@ExtendWith(MockitoExtension.class)
public class FlightProviderClientBTest {
  @Mock
  private WebServiceTemplate webServiceTemplate;

  @InjectMocks
  private FlightProviderBClient flightProviderBClient;

  private SearchRequestB request;

  @BeforeEach
  void setUp() {
    request = new SearchRequestB("IST", "LHR", java.time.LocalDateTime.now());
  }

  @Test
  void testCallAvailabilitySearch_Success() {
    // Arrange
    SearchResultB expectedResult = new SearchResultB();
    expectedResult.setHasError(false);

    when(webServiceTemplate.marshalSendAndReceive(
        eq("http://localhost:8081/ws"),
        eq(request),
        any(SoapActionCallback.class)))
        .thenReturn(expectedResult);

    // Act
    CompletableFuture<SearchResultB> result = flightProviderBClient.callAvailabilitySearch(request);

    // Assert
    assertNotNull(result);
    assertTrue(result.isDone());
    assertFalse(result.join().isHasError());

    verify(webServiceTemplate).marshalSendAndReceive(
        eq("http://localhost:8081/ws"),
        eq(request),
        any(SoapActionCallback.class));
  }
}
