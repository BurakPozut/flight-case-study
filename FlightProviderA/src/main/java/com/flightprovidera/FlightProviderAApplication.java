package com.flightprovidera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.flightprovidera")
public class FlightProviderAApplication {
  public static void main(String[] args) {
    SpringApplication.run(FlightProviderAApplication.class, args);
  }
}
