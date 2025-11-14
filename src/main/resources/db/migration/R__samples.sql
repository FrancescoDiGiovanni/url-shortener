INSERT INTO url_mappings (short_id, original_url, hit_count, last_accessed, expiration)
VALUES
    ('1', 'https://www.google.com', 15, DATEADD('HOUR', -2, CURRENT_TIMESTAMP), NULL),
    ('10', 'https://spring.io/projects/spring-boot', 5, DATEADD('DAY', -1, CURRENT_TIMESTAMP), NULL),
    ('g7', 'https://github.com/spring-projects/spring-boot', 0, NULL, NULL),
    ('q0p', 'https://www.example.com', 3, DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', 5, CURRENT_TIMESTAMP))