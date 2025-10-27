package com.flightaggregator.flight_aggregator_api.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FlightProviderAClient {

  private final RestTemplate restTemplate = new RestTemplate();
  private static final String PROVIDER_A_URL = "http://localhost:8080/ws";

  public String callAvailabilitySearch(String xmlRequest) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_XML);
    headers.set("SOAPAction", "\"http://flightprovidera.com/availabilitySearchRequest\"");

    HttpEntity<String> request = new HttpEntity<>(xmlRequest, headers);
    ResponseEntity<String> response = restTemplate.postForEntity(
        PROVIDER_A_URL, request, String.class);

    return response.getBody();
  }
}
