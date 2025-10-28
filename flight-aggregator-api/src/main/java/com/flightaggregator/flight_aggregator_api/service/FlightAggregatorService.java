package com.flightaggregator.flight_aggregator_api.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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
            "FlightProviderB"));
      }
    }

    return flights;
  }

  // public List<FlightResponse> getCheapestFlights(FlightSearchRequest request) {
  // List<FlightResponse> allFlights = searchFlightsFromProviderA(request);
  // allFlights.addAll(searchFlightsFromProviderB(request));

  // Map<String, FlightResponse> cheapestFlights = new HashMap<>();

  // for (FlightResponse flightResponse : allFlights) {
  // String key = flightResponse.getFlightNo() + "_" +
  // flightResponse.getDepartureDateTime();

  // if (!cheapestFlights.containsKey(key) ||
  // flightResponse.getPrice().compareTo(cheapestFlights.get(key).getPrice()) < 0)
  // {
  // cheapestFlights.put(key, flightResponse);
  // }
  // }

  // return new ArrayList<>(cheapestFlights.values());

  // }

  public List<FlightResponse> getCheapestFlights(FlightSearchRequest request) {
    List<FlightResponse> flightsA = searchFlightsFromProviderA(request);
    List<FlightResponse> flightsB = searchFlightsFromProviderB(request);

    int excepted = flightsA.size() + flightsB.size();

    Map<FlightKey, FlightResponse> cheapest = new HashMap<>((int) Math.ceil(excepted / 0.75));

    for (FlightResponse fr : flightsA)
      cheapest.merge(keyOf(fr), fr, FlightAggregatorService::cheaper);
    for (FlightResponse fr : flightsB)
      cheapest.merge(keyOf(fr), fr, FlightAggregatorService::cheaper);

    return cheapest.values().stream().sorted(Comparator.comparing(FlightResponse::getPrice)).toList();
  }

  private static FlightKey keyOf(FlightResponse fr) {
    return new FlightKey(
        fr.getFlightNo().toUpperCase(Locale.ROOT),
        fr.getOrigin().toUpperCase(Locale.ROOT),
        fr.getDestination().toUpperCase(Locale.ROOT),
        fr.getDepartureDateTime());// TODO we need to change this to use Instant not local datet time
  }

  private static FlightResponse cheaper(FlightResponse x, FlightResponse y) {
    return y.getPrice().compareTo(x.getPrice()) < 0 ? y : x;
  }

  record FlightKey(String flightNo, String origin, String destination, LocalDateTime departure) {
  }
}
