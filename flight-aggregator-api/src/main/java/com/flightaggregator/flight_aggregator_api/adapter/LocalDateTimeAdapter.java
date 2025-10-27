package com.flightaggregator.flight_aggregator_api.adapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  @Override
  public LocalDateTime unmarshal(String v) {
    return LocalDateTime.parse(v, formatter);
  }

  @Override
  public String marshal(LocalDateTime v) throws Exception {
    return v.format(formatter);
  }

}
