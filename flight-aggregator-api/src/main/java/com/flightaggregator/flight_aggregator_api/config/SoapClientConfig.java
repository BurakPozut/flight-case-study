package com.flightaggregator.flight_aggregator_api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
public class SoapClientConfig {

  @Bean("providerAMarshaller")
  public Jaxb2Marshaller providerAMarshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setPackagesToScan("com.flightaggregator.flight_aggregator_api.model.providerA");
    return marshaller;
  }

  @Bean("providerBMarshaller")
  public Jaxb2Marshaller providerBMarshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setPackagesToScan("com.flightaggregator.flight_aggregator_api.model.providerB");
    return marshaller;
  }

  @Bean("providerATemplate")
  public WebServiceTemplate providerATemplate(@Qualifier("providerAMarshaller") Jaxb2Marshaller marshaller) {
    WebServiceTemplate template = new WebServiceTemplate();
    template.setMarshaller(marshaller);
    template.setUnmarshaller(marshaller);
    return template;
  }

  @Bean("providerBTemplate")
  public WebServiceTemplate providerBTemplate(@Qualifier("providerBMarshaller") Jaxb2Marshaller marshaller) {
    WebServiceTemplate template = new WebServiceTemplate();
    template.setMarshaller(marshaller);
    template.setUnmarshaller(marshaller);
    return template;
  }
}
