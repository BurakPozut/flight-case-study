package com.flightaggregator.flight_aggregator_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flightaggregator.flight_aggregator_api.dto.FlightResponse;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchRequest;
import com.flightaggregator.flight_aggregator_api.model.providerA.Flight;
import com.flightaggregator.flight_aggregator_api.model.providerA.SearchRequest;
import com.flightaggregator.flight_aggregator_api.model.providerA.SearchResult;

@Service
public class FlightAggregatorService {

  @Autowired
  private FlightProviderAClient providerAClient;

  public List<FlightResponse> searchFlightsFromProviderA(FlightSearchRequest request) {
    // Convert REST DTO to SOAP model
    SearchRequest soapRequest = new SearchRequest(
        request.getOrigin(),
        request.getDestination(),
        request.getDepartureDate());

    // Call SOAP service
    SearchResult soapResponse = providerAClient.callAvailabilitySearch(soapRequest);

    // Convert SOAP response to REST DTOs
    List<FlightResponse> flights = new ArrayList<>();
    if (!soapResponse.isHasError()) {
      for (Flight flight : soapResponse.getFlightOptions()) {
        flights.add(new FlightResponse(
            flight.getFlightNo(),
            flight.getOrigin(),
            flight.getDestination(),
            flight.getDeparturedatetime(),
            flight.getArrivaldatetime(),
            flight.getPrice(),
            "FlightProviderA"));
      }
    }

    return flights;
  }
}
