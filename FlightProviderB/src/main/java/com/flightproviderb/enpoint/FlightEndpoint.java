package com.flightproviderb.enpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.flightproviderb.model.SearchRequest;
import com.flightproviderb.model.SearchResult;
import com.flightproviderb.service.SearchService;

@Endpoint
public class FlightEndpoint {
  private static final String NAMESPACE_URI = "http://flightproviderb.com";

  @Autowired
  private SearchService searchService;

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "availabilitySearchRequest")
  @ResponsePayload
  public SearchResult availabilitySearch(@RequestPayload SearchRequest request) {
    return searchService.availabilitySearch(request);
  }

}
