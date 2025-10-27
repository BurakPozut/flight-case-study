package com.flightaggregator.flight_aggregator_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flightaggregator.flight_aggregator_api.dto.FlightResponse;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchRequest;

@Service
public class FlightAggregatorService {

  @Autowired
  private FlightProviderAClient providerAClient;

  public List<FlightResponse> searchFlightsFromProviderA(FlightSearchRequest request) {
    // Create SOAP XML manually
    String soapRequest = createSoapRequest(request);

    // Call provider
    String soapResponse = providerAClient.callAvailabilitySearch(soapRequest);
    System.out.println("===== Soap Response ======= \n" + soapResponse);

    // Parse response (for now, return empty list)
    return new ArrayList<>();
  }

  private String createSoapRequest(FlightSearchRequest request) {
    return String.format("""
        <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                       xmlns:tns="http://flightprovidera.com">
            <soap:Body>
                <tns:availabilitySearchRequest>
                    <tns:origin>%s</tns:origin>
                    <tns:destination>%s</tns:destination>
                    <tns:departureDate>%s</tns:departureDate>
                </tns:availabilitySearchRequest>
            </soap:Body>
        </soap:Envelope>
        """, request.getOrigin(), request.getDestination(), request.getDepartureDate());
  }
}
