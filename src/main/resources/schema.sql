-- Create events table for H2 database
CREATE TABLE IF NOT EXISTS events (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date TIMESTAMP,
    capacity INT,
    half_price_limit INT,
    details VARCHAR(4000),
    location VARCHAR(255),
    ticket_price DECIMAL(19, 2)
);

-- Create index for common queries
CREATE INDEX IF NOT EXISTS idx_event_name ON events(name);

