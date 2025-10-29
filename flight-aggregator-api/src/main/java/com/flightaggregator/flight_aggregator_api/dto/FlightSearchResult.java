package com.flightaggregator.flight_aggregator_api.dto;

import java.util.List;

public class FlightSearchResult {
  private List<FlightResponse> flights;
  private FlightSearchMetadata metadata;

  public FlightSearchResult() {
  }

  public FlightSearchResult(List<FlightResponse> flights, FlightSearchMetadata metadata) {
    this.flights = flights;
    this.metadata = metadata;
  }

  public List<FlightResponse> getFlights() {
    return flights;
  }

  public void setFlights(List<FlightResponse> flights) {
    this.flights = flights;
  }

  public FlightSearchMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(FlightSearchMetadata metadata) {
    this.metadata = metadata;
  }
}