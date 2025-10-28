package com.flightaggregator.flight_aggregator_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.flightaggregator.flight_aggregator_api.model.providerB.SearchRequestB;
import com.flightaggregator.flight_aggregator_api.model.providerB.SearchResultB;

@Service
public class FlightProviderBClient {

  private static final String PROVIDER_A_URL = "http://localhost:8081/ws";

  @Autowired
  @Qualifier("providerBTemplate")
  private WebServiceTemplate webServiceTemplate;

  public SearchResultB callAvailabilitySearch(SearchRequestB request) {
    return (SearchResultB) webServiceTemplate.marshalSendAndReceive(PROVIDER_A_URL, request,
        new SoapActionCallback("http://flightproviderb.com/availabilitySearchRequest"));
  }
}
