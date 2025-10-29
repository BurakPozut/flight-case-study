package com.flightaggregator.flight_aggregator_api.service;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.flightaggregator.flight_aggregator_api.model.providerB.SearchRequestB;
import com.flightaggregator.flight_aggregator_api.model.providerB.SearchResultB;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@Service
public class FlightProviderBClient {

  private static final Logger logger = LoggerFactory.getLogger(FlightProviderBClient.class);

  // private static final String PROVIDER_A_URL = "http://localhost:8081/ws";
  @Value("${provider.b.url:http://localhost:8081/ws}")
  private String providerBUrl;

  @Autowired
  @Qualifier("providerBTemplate")
  private WebServiceTemplate webServiceTemplate;

  @CircuitBreaker(name = "providerB", fallbackMethod = "callAvailabilitySearchFallback")
  @Retry(name = "providerB")
  @TimeLimiter(name = "providerB")
  public CompletableFuture<SearchResultB> callAvailabilitySearch(SearchRequestB request) {
    SearchResultB result = (SearchResultB) webServiceTemplate.marshalSendAndReceive(providerBUrl, request,
        new SoapActionCallback("http://flightproviderb.com/availabilitySearchRequest"));

    return CompletableFuture.completedFuture(result);
  }

  @SuppressWarnings("unused")
  private CompletableFuture<SearchResultB> callAvailabilitySearchFallback(
      SearchRequestB requestA, Exception ex) {
    logger.warn("⚠️ CIRCUIT BREAKER FALLBACK TRIGGERED for Provider B - Exception: {} - Message: {}",
        ex != null ? ex.getClass().getSimpleName() : "Unknown",
        ex != null ? ex.getMessage() : "Circuit breaker opened");
    SearchResultB fallbackResult = new SearchResultB();
    fallbackResult.setHasError(true);
    return CompletableFuture.completedFuture(fallbackResult);
  }
}
