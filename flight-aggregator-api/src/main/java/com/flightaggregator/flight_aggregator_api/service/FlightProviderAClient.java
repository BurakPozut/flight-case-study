package com.flightaggregator.flight_aggregator_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.flightaggregator.flight_aggregator_api.model.providerA.SearchRequestA;
import com.flightaggregator.flight_aggregator_api.model.providerA.SearchResultA;

@Service
public class FlightProviderAClient {// TODO add These circut breaker resilliance 4j

  private static final String PROVIDER_A_URL = "http://localhost:8080/ws";

  @Autowired
  @Qualifier("providerATemplate")
  private WebServiceTemplate webServiceTemplate;

  public SearchResultA callAvailabilitySearch(SearchRequestA request) {
    return (SearchResultA) webServiceTemplate.marshalSendAndReceive(PROVIDER_A_URL, request,
        new SoapActionCallback("http://flightprovidera.com/availabilitySearchRequest"));
  }
}
