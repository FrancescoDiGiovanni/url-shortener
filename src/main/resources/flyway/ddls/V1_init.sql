CREATE TABLE url_mapping (
     id BIGINT PRIMARY KEY AUTO_INCREMENT,
     short_id VARCHAR(20) NOT NULL UNIQUE,
     original_url TEXT NOT NULL,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     expiration TIMESTAMP NULL,
     hit_count BIGINT NOT NULL DEFAULT 0,
     last_accessed TIMESTAMP NULL
);