package com.flightaggregator.flight_aggregator_api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

  public List<FlightResponse> getCheapestFlights(FlightSearchRequest request) {
    List<FlightResponse> flightsA = searchFlightsFromProviderA(request);
    List<FlightResponse> flightsB = searchFlightsFromProviderB(request);

    // Used excepted to overcome the map resing. Map object load factor is 0.75. So
    // if we give it a capacity of 10 it will resize when we added the 8th element
    // (10 * 0.75 = 7).
    // Thats why we give the capacity little higher than we need, not the exact
    // number
    int excepted = flightsA.size() + flightsB.size();
    Map<FlightKey, FlightResponse> cheapest = new HashMap<>((int) Math.ceil(excepted / 0.75));

    for (FlightResponse fr : flightsA)
      cheapest.put(keyOf(fr), fr); // The map is empty there is no need for merge
    for (FlightResponse fr : flightsB)
      cheapest.merge(keyOf(fr), fr, FlightAggregatorService::cheaper);

    return cheapest.values().stream().sorted(Comparator.comparing(FlightResponse::getPrice)).toList();
  }

  private static FlightKey keyOf(FlightResponse fr) {
    return new FlightKey(
        fr.getFlightNo().toUpperCase(Locale.ROOT),
        fr.getOrigin().toUpperCase(Locale.ROOT),
        fr.getDestination().toUpperCase(Locale.ROOT),
        fr.getDepartureDateTime());// TODO we need to change this to use Instant not local date time
  }

  private static FlightResponse cheaper(FlightResponse x, FlightResponse y) {
    // TODO: Handle currency mismatch (right now we don't need it)
    return y.getPrice().compareTo(x.getPrice()) < 0 ? y : x;
  }

  /**
   * Represents a unique flight identity.
   * 'departure' currently uses LocalDateTime because providers do not supply
   * timezone info.
   * When available, replace with Instant normalized to UTC.
   */
  record FlightKey(String flightNo, String origin, String destination, LocalDateTime departure) {
  }
}
