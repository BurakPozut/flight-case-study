package com.flightaggregator.flight_aggregator_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
public class SoapClientConfig {

  @Bean
  public Jaxb2Marshaller marshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setPackagesToScan("com.flightaggregator.flight_aggregator_api.model.providerA",
    "com.flightaggregator.flight_aggregator_api.model.providerB");
    return marshaller;
  }

  @Bean
  public WebServiceTemplate webServiceTemplate() {
    WebServiceTemplate template = new WebServiceTemplate();
    template.setMarshaller(marshaller());
    template.setUnmarshaller(marshaller());
    return template;
  }
}
