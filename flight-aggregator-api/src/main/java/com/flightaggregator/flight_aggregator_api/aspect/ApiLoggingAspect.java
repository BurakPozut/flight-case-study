package com.flightaggregator.flight_aggregator_api.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightaggregator.flight_aggregator_api.service.ApiLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ApiLoggingAspect {

  private static final Logger logger = LoggerFactory.getLogger(ApiLoggingAspect.class);

  @Autowired
  private ApiLogService apiLogService;

  @Autowired
  private ObjectMapper objectMapper;

  @Around("(@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
      "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
      "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
      "@annotation(org.springframework.web.bind.annotation.DeleteMapping)) && " +
      "!execution(* com.flightaggregator.flight_aggregator_api.controller.ApiLogController.*(..))")
  public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {

    long startTime = System.currentTimeMillis();

    // Get request information
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .getRequest();
    String endpoint = request.getRequestURI();
    String method = request.getMethod();

    // Prepare request data
    Map<String, Object> requestData = new HashMap<>();
    requestData.put("parameters", request.getParameterMap());
    requestData.put("pathVariables", extractPathVariables(joinPoint));
    requestData.put("queryString", request.getQueryString());

    String requestDataJson = objectMapper.writeValueAsString(requestData);

    Object result = null;
    String responseDataJson = null;
    String provider = "REST_API"; // Default provider
    @SuppressWarnings("unused")
    Exception exception = null;

    try {
      // Execute the method
      result = joinPoint.proceed();

      // Extract response data
      if (result instanceof ResponseEntity) {
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
        responseDataJson = objectMapper.writeValueAsString(responseEntity.getBody());
      } else {
        responseDataJson = objectMapper.writeValueAsString(result);
      }

      // Determine provider based on endpoint
      provider = determineProvider(endpoint);

    } catch (Exception e) {
      exception = e;
      responseDataJson = "{\"error\": \"" + e.getMessage() + "\"}";
      throw e; // Re-throw the exception
    } finally {
      // Calculate response time
      long endTime = System.currentTimeMillis();
      int responseTimeMs = (int) (endTime - startTime);

      // Log to database
      try {
        apiLogService.saveApiLog(
            endpoint,
            method,
            requestDataJson,
            responseDataJson,
            responseTimeMs,
            provider);
      } catch (Exception e) {
        logger.error("Failed to save API log", e);
      }

      // Log to console
      logger.info("API Call - Endpoint: {}, Method: {}, Response Time: {}ms, Provider: {}",
          endpoint, method, responseTimeMs, provider);
    }

    return result;
  }

  private Map<String, Object> extractPathVariables(ProceedingJoinPoint joinPoint) {
    Map<String, Object> pathVariables = new HashMap<>();
    Object[] args = joinPoint.getArgs();
    String[] paramNames = getParameterNames(joinPoint);

    for (int i = 0; i < args.length; i++) {
      if (paramNames != null && i < paramNames.length) {
        pathVariables.put(paramNames[i], args[i]);
      }
    }

    return pathVariables;
  }

  private String[] getParameterNames(ProceedingJoinPoint joinPoint) {
    // This is a simplified version - in production you might want to use more
    // sophisticated reflection
    return Arrays.stream(joinPoint.getArgs())
        .map(arg -> arg != null ? arg.getClass().getSimpleName() : "null")
        .toArray(String[]::new);
  }

  private String determineProvider(String endpoint) {
    if (endpoint.contains("/provider-a")) {
      return "FlightProviderA";
    } else if (endpoint.contains("/provider-b")) {
      return "FlightProviderB";
    } else if (endpoint.contains("/cheapest")) {
      return "FlightAggregator";
    } else {
      return "REST_API";
    }
  }
}