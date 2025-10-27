package com.flightaggregator.flight_aggregator_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.flightaggregator.flight_aggregator_api.model.providerA.SearchRequest;
import com.flightaggregator.flight_aggregator_api.model.providerA.SearchResult;

@Service
public class FlightProviderAClient {

  private static final String PROVIDER_A_URL = "http://localhost:8080/ws";

  @Autowired
  private WebServiceTemplate webServiceTemplate;

  public SearchResult callAvailabilitySearch(SearchRequest request) {
    return (SearchResult) webServiceTemplate.marshalSendAndReceive(PROVIDER_A_URL, request,
        new SoapActionCallback("http://flightprovidera.com/availabilitySearchRequest"));
  }
}
