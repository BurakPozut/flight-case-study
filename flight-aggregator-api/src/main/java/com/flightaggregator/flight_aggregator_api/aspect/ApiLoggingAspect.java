package com.flightaggregator.flight_aggregator_api.aspect;

import com.flightaggregator.flight_aggregator_api.dto.FlightResponse;
import com.flightaggregator.flight_aggregator_api.dto.FlightSearchMetadata;
import com.flightaggregator.flight_aggregator_api.service.ApiLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Aspect
@Component
public class ApiLoggingAspect {

  private static final Logger logger = LoggerFactory.getLogger(ApiLoggingAspect.class);

  @Autowired
  private ApiLogService apiLogService;

  @Around("(@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
      "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
      "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
      "@annotation(org.springframework.web.bind.annotation.DeleteMapping)) && " +
      "!execution(* com.flightaggregator.flight_aggregator_api.controller.ApiLogController.*(..)) && " +
      "!execution(* org.springdoc..*(..)) && " +
      "!within(org.springdoc..*)")
  public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {

    long startTime = System.currentTimeMillis();

    // Get request information
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .getRequest();
    String endpoint = request.getRequestURI();
    String method = request.getMethod();

    // Skip logging for Swagger/OpenAPI endpoints
    if (endpoint.startsWith("/api-docs") ||
        endpoint.startsWith("/swagger-ui") ||
        endpoint.startsWith("/swagger-resources") ||
        endpoint.startsWith("/webjars") ||
        endpoint.contains("swagger")) {
      return joinPoint.proceed();
    }

    // Extract flight search metadata from the request
    FlightSearchMetadata metadata = extractFlightSearchMetadata(joinPoint, endpoint);

    Object result = null;
    Integer statusCode = 200;

    try {
      // Execute the method
      result = joinPoint.proceed();

      // Extract status code and flight data for metadata
      if (result instanceof ResponseEntity) {
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
        statusCode = responseEntity.getStatusCode().value();

        // Try to get metadata from request scope first
        FlightSearchMetadata requestMetadata = (FlightSearchMetadata) request.getAttribute("flightSearchMetadata");
        if (requestMetadata != null) {
          metadata.setProviderALatencyMs(requestMetadata.getProviderALatencyMs());
          metadata.setProviderBLatencyMs(requestMetadata.getProviderBLatencyMs());
          metadata.setProviderACount(requestMetadata.getProviderACount());
          metadata.setProviderBCount(requestMetadata.getProviderBCount());
          metadata.setMinPrice(requestMetadata.getMinPrice());
          metadata.setMaxPrice(requestMetadata.getMaxPrice());
          metadata.setTotalFlights(requestMetadata.getTotalFlights());

        } else {
          // Fallback: extract flight data for metadata
          if (responseEntity.getBody() instanceof List) {
            List<?> body = (List<?>) responseEntity.getBody();
            if (body != null && !body.isEmpty() && body.get(0) instanceof FlightResponse) {
              @SuppressWarnings("unchecked")
              List<FlightResponse> flights = (List<FlightResponse>) body;
              metadata.updateWithFlightData(flights);
            }
          }
        }
      }

    } catch (Exception e) {
      statusCode = 500;
      throw e; // Re-throw the exception
    } finally {
      // Calculate response time
      long endTime = System.currentTimeMillis();
      int durationMs = (int) (endTime - startTime);

      // Log to database using the new metadata approach
      try {
        apiLogService.saveApiLog(metadata, statusCode, durationMs);
      } catch (Exception e) {
        logger.error("Failed to save API log", e);
      }

      // Log to console
      logger.info("API Call - Endpoint: {}, Method: {}, Status: {}, Duration: {}ms, " +
          "Origin: {}, Destination: {}, Total Flights: {}",
          endpoint, method, statusCode, durationMs,
          metadata.getOrigin(), metadata.getDestination(), metadata.getTotalFlights());
    }

    return result;
  }

  /**
   * Extract flight search metadata from the method parameters
   */
  private FlightSearchMetadata extractFlightSearchMetadata(ProceedingJoinPoint joinPoint, String endpoint) {
    FlightSearchMetadata metadata = new FlightSearchMetadata(endpoint);

    Object[] args = joinPoint.getArgs();
    Signature signature = joinPoint.getSignature();

    if (signature instanceof MethodSignature) {
      MethodSignature methodSignature = (MethodSignature) signature;
      String[] paramNames = methodSignature.getParameterNames();

      for (int i = 0; i < args.length; i++) {
        if (paramNames != null && i < paramNames.length && args[i] != null) {
          String paramName = paramNames[i];
          Object arg = args[i];

          switch (paramName) {
            case "origin":
              metadata.setOrigin((String) arg);
              break;
            case "destination":
              metadata.setDestination((String) arg);
              break;
            case "departureDate":
              if (arg instanceof LocalDateTime) {
                metadata.setDepartureDate(((LocalDateTime) arg).toLocalDate());
              } else if (arg instanceof String) {
                // Handle String format for provider-a and provider-b endpoints
                try {
                    LocalDateTime dateTime = LocalDateTime.parse((String) arg);
                    metadata.setDepartureDate(dateTime.toLocalDate());
                } catch (Exception e) {
                    logger.warn("Failed to parse departure date string: {}", arg);
                }
              }
              break;
            case "destionation": // Handle typo in controller
              metadata.setDestination((String) arg);
              break;
            case "departure": // For provider-b endpoint
              metadata.setOrigin((String) arg);
              break;
            case "arrival": // For provider-b endpoint
              metadata.setDestination((String) arg);
              break;
          }
        }
      }
    }

    return metadata;
  }
}