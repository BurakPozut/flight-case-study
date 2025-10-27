package com.flightaggregator.flight_aggregator_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flightaggregator.flight_aggregator_api.dto.FlightResponse;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchRequest;
import com.flightaggregator.flight_aggregator_api.model.providerA.FlightA;
import com.flightaggregator.flight_aggregator_api.model.providerA.SearchRequestA;
import com.flightaggregator.flight_aggregator_api.model.providerA.SearchResultA;
import com.flightaggregator.flight_aggregator_api.model.providerB.FlightB;
import com.flightaggregator.flight_aggregator_api.model.providerB.SearchRequestB;
import com.flightaggregator.flight_aggregator_api.model.providerB.SearchResultB;

@Service
public class FlightAggregatorService {

  @Autowired
  private FlightProviderAClient providerAClient;

  @Autowired
  private FlightProviderBClient providerBClient;

  public List<FlightResponse> searchFlightsFromProviderA(FlightSearchRequest request) {
    SearchRequestA soapRequest = new SearchRequestA(
        request.getOrigin(),
        request.getDestination(),
        request.getDepartureDate());

    SearchResultA soapResponse = providerAClient.callAvailabilitySearch(soapRequest);

    List<FlightResponse> flights = new ArrayList<>();
    if (!soapResponse.isHasError()) {
      for (FlightA flight : soapResponse.getFlightOptions()) {
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

  public List<FlightResponse> searchFlightsFromProviderB(FlightSearchRequest request) {
    SearchRequestB soapRequest = new SearchRequestB(
        request.getOrigin(),
        request.getDestination(),
        request.getDepartureDate());

    SearchResultB soapResponse = providerBClient.callAvailabilitySearch(soapRequest);

    List<FlightResponse> flights = new ArrayList<>();
    if (!soapResponse.isHasError()) {
      for (FlightB flight : soapResponse.getFlightOptions()) {
        flights.add(new FlightResponse(
            flight.getFlightNumber(),
            flight.getDeparture(),
            flight.getArrival(),
            flight.getDeparturedatetime(),
            flight.getArrivaldatetime(),
            flight.getPrice(),
            "FlightProviderA"));
      }
    }

    return flights;
  }
  
}
