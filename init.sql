-- Create the api_logs table
CREATE TABLE api_logs (
    id BIGSERIAL PRIMARY KEY,
    endpoint VARCHAR(255),           -- "/api/v1/flights/IST/COV/2025-12-15T10:00:00"
    request_method VARCHAR(10),     -- "GET"
    request_data TEXT,              -- JSON of request parameters
    response_data TEXT,             -- JSON of response
    response_time_ms INTEGER,       -- How long it took
    provider VARCHAR(50),           -- "FlightProviderA", "FlightProviderB", or "REST_API"
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create an index on created_at for better query performance
CREATE INDEX idx_api_logs_created_at ON api_logs(created_at);

-- Create an index on provider for filtering by provider
CREATE INDEX idx_api_logs_provider ON api_logs(provider);

-- Create an index on endpoint for filtering by endpoint
CREATE INDEX idx_api_logs_endpoint ON api_logs(endpoint);