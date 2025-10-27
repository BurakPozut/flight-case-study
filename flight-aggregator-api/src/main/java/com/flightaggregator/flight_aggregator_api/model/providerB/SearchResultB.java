package com.flightaggregator.flight_aggregator_api.model.providerB;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "availabilitySearchResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchResultB {
	private boolean hasError = false;

	private List<FlightB> flightOptions = new ArrayList<>();
	private String errorMessage;

	// Constructors
	public SearchResultB() {
	}

	public SearchResultB(boolean hasError, List<FlightB> flightOptions, String errorMessage) {
		this.hasError = hasError;
		this.flightOptions = flightOptions;
		this.errorMessage = errorMessage;
	}

	// Getters and Setters
	public boolean isHasError() {
		return hasError;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public List<FlightB> getFlightOptions() {
		return flightOptions;
	}

	public void setFlightOptions(List<FlightB> flightOptions) {
		this.flightOptions = flightOptions;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}