package com.flightproviderb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.flightproviderb")
public class FlightProviderBApplication {

  public static void main(String[] args) {
    SpringApplication.run(FlightProviderBApplication.class, args);
  }
}
