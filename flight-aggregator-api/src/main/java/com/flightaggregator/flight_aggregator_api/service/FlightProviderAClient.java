package com.flightaggregator.flight_aggregator_api.service;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.flightaggregator.flight_aggregator_api.model.providerA.SearchRequestA;
import com.flightaggregator.flight_aggregator_api.model.providerA.SearchResultA;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@Service
public class FlightProviderAClient {// TODO add These circut breaker resilliance 4j

  private static final Logger logger = LoggerFactory.getLogger(FlightProviderAClient.class);

  private static final String PROVIDER_A_URL = "http://localhost:8080/ws";

  @Autowired
  @Qualifier("providerATemplate")
  private WebServiceTemplate webServiceTemplate;

  @CircuitBreaker(name = "providerA", fallbackMethod = "callAvailabilitySearchFallback")
  @Retry(name = "providerA")
  @TimeLimiter(name = "providerA")
  public CompletableFuture<SearchResultA> callAvailabilitySearch(SearchRequestA request) {
    SearchResultA result = (SearchResultA) webServiceTemplate.marshalSendAndReceive(PROVIDER_A_URL, request,
        new SoapActionCallback("http://flightprovidera.com/availabilitySearchRequest"));

    return CompletableFuture.completedFuture(result);
  }

  public CompletableFuture<SearchResultA> callAvailabilitySearchFallback(
      SearchRequestA requestA, Exception ex) {
    logger.warn("⚠️ CIRCUIT BREAKER FALLBACK TRIGGERED for Provider A - Exception: {} - Message: {}",
        ex != null ? ex.getClass().getSimpleName() : "Unknown",
        ex != null ? ex.getMessage() : "Circuit breaker opened");
    SearchResultA fallbackResult = new SearchResultA();
    fallbackResult.setHasError(true);
    return CompletableFuture.completedFuture(fallbackResult);
  }
}
