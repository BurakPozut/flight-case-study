package com.flightprovidera.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.flightprovidera.model.SearchRequest;
import com.flightprovidera.model.SearchResult;
import com.flightprovidera.service.SearchService;

@Endpoint
public class FlightEndpoint {
  private static final String NAMESPACE_URI = "http://flightprovidera.com";

  @Autowired
  private SearchService searchService;

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "availabilitySearchRequest")
  @ResponsePayload
  public SearchResult availabilitySearch(@RequestPayload SearchRequest request) {
    return searchService.availabilitySearch(request);
  }
}