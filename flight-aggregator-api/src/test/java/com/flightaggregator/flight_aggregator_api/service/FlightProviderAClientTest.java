package com.flightaggregator.flight_aggregator_api.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.flightaggregator.flight_aggregator_api.model.providerA.SearchRequestA;
import com.flightaggregator.flight_aggregator_api.model.providerA.SearchResultA;

@ExtendWith(MockitoExtension.class)
public class FlightProviderAClientTest {

  @Mock
  private WebServiceTemplate webServiceTemplate;

  @InjectMocks
  private FlightProviderAClient flightProviderAClient;

  private SearchRequestA request;

  @BeforeEach
  void setup() {
    request = new SearchRequestA("IST", "COV", LocalDateTime.now());
  }

  @Test
  void testCallAvailabilitySearch_Success() {
    SearchResultA expectedResutl = new SearchResultA();
    expectedResutl.setHasError(false);

    when(webServiceTemplate.marshalSendAndReceive(eq("http://localhost:8080/ws"),
        eq(request),
        any(SoapActionCallback.class))).thenReturn(expectedResutl);

    CompletableFuture<SearchResultA> result = flightProviderAClient.callAvailabilitySearch(request);

    assertNotNull(result);
    assertTrue(result.isDone());
    assertFalse(result.join().isHasError());

    verify(webServiceTemplate).marshalSendAndReceive(
        eq("http://localhost:8080/ws"),
        eq(request),
        any(SoapActionCallback.class));
  }
}
