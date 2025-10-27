package com.flightproviderb.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.flightproviderb.adapter.LocalDateTimeAdapter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "Flight")
@XmlAccessorType(XmlAccessType.FIELD)
public class Flight {

	private String flightNumber;
	private String departure;
	private String arrival;
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime departuredatetime;
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime arrivaldatetime;
	private BigDecimal price;

	public Flight() {
	}

	public Flight(String flightNumber, String departure, String arrival, LocalDateTime departuredatetime,
			LocalDateTime arrivaldatetime, BigDecimal price) {
		super();
		this.flightNumber = flightNumber;
		this.departure = departure;
		this.arrival = arrival;
		this.departuredatetime = departuredatetime;
		this.arrivaldatetime = arrivaldatetime;
		this.price = price;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public void setDestination(String destination) {
		this.arrival = destination;
	}

	public LocalDateTime getDeparturedatetime() {
		return departuredatetime;
	}

	public void setDeparturedatetime(LocalDateTime departuredatetime) {
		this.departuredatetime = departuredatetime;
	}

	public LocalDateTime getArrivaldatetime() {
		return arrivaldatetime;
	}

	public void setArrivaldatetime(LocalDateTime arrivaldatetime) {
		this.arrivaldatetime = arrivaldatetime;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
