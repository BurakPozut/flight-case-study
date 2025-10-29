-- Drop the old table and create the new one
DROP TABLE IF EXISTS api_logs;

-- Create the new api_logs table with metadata structure
CREATE TABLE api_logs (
    id BIGSERIAL PRIMARY KEY,
    endpoint VARCHAR(255),                    -- "/api/v1/flights/cheapest/{origin}/{destination}/{date}"
    method VARCHAR(10),                      -- "GET"
    status INTEGER,                          -- HTTP status code
    duration_ms INTEGER,                      -- Total request duration
    origin VARCHAR(10),                      -- "IST"
    destination VARCHAR(10),                 -- "COV"
    departure_date DATE,                     -- "2026-01-01"
    provider_a_latency_ms INTEGER,           -- Provider A response time
    provider_b_latency_ms INTEGER,           -- Provider B response time
    provider_a_count INTEGER,               -- Number of flights from Provider A
    provider_b_count INTEGER,               -- Number of flights from Provider B
    min_price DECIMAL(10,2),                 -- Minimum price found
    max_price DECIMAL(10,2),                 -- Maximum price found
    total_flights INTEGER,                   -- Total number of flights returned
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_api_logs_created_at ON api_logs(created_at);
CREATE INDEX idx_api_logs_endpoint ON api_logs(endpoint);
CREATE INDEX idx_api_logs_origin_destination ON api_logs(origin, destination);
CREATE INDEX idx_api_logs_departure_date ON api_logs(departure_date);