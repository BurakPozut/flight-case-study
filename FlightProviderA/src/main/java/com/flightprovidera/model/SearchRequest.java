package com.flightprovidera.model;

import java.time.LocalDateTime;

import com.flightprovidera.adapter.LocalDateTimeAdapter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "availabilitySearchRequest", namespace = "http://flightprovidera.com")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchRequest {
	@XmlElement(namespace = "http://flightprovidera.com")
	private String origin = "";
	@XmlElement(namespace = "http://flightprovidera.com")
	private String destination = "";
	@XmlElement(namespace = "http://flightprovidera.com")
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime departureDate;

	// Constructors
	public SearchRequest() {
	}

	public SearchRequest(String origin, String destination, LocalDateTime departureDate) {
		this.origin = origin;
		this.destination = destination;
		this.departureDate = departureDate;
	}

	// Getters and Setters
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public LocalDateTime getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDateTime departureDate) {
		this.departureDate = departureDate;
	}
}
