package com.flightaggregator.flight_aggregator_api.model.providerB;

import java.time.LocalDateTime;

import com.flightaggregator.flight_aggregator_api.adapter.LocalDateTimeAdapter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "availabilitySearchRequest", namespace = "http://flightproviderb.com")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchRequestB {

	@XmlElement(namespace = "http://flightproviderb.com")
	private String departure = "";

	@XmlElement(namespace = "http://flightproviderb.com")
	private String arrival = "";

	@XmlElement(namespace = "http://flightproviderb.com")
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime departureDate;

	// Constructors
	public SearchRequestB() {
	}

	public SearchRequestB(String departure, String arrival, LocalDateTime departureDate) {
		this.departure = departure;
		this.arrival = arrival;
		this.departureDate = departureDate;
	}

	// Getters and Setters
	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String origin) {
		this.departure = origin;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String destination) {
		this.arrival = destination;
	}

	public LocalDateTime getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDateTime departureDate) {
		this.departureDate = departureDate;
	}
}
