<h1>URL Shortening Service</h1>

<p>A Spring Boot–based URL Shortener application providing REST APIs for generating Base-62 short URLs and retrieving the corresponding original URLs.</p>
<p>The service includes environment-specific configurations, database migrations, metrics monitoring, and complete API documentation via Swagger.</p>

<hr>

<h2>1 - Features</h2>
<ul>
  <li>Generate Base-62 short URLs</li>
  <li>Retrieve original URLs starting from a shortId</li>
  <li>MySQL support (production)</li>
  <li>H2 in-memory DB + Flyway (development & test)</li>
  <li>Actuator + Prometheus metrics</li>
  <li>Swagger documentation (OpenAPI)</li>
  <li>Layered architecture (Controller → Service → Repository → DB)</li>
  <li>Unit & Integration tests included</li>
</ul>

<hr>

<h2>2 - REST API Overview</h2>

<h3>2.1 - POST <code>/shortener/</code> – Create a short URL</h3>
<p>Creates and stores a Base-62 shortId from an original URL.</p>

<h4>Request Body</h4>
<pre>
{
  "originalUrl": "https://example.com"
}
</pre>

<h4>Response Example</h4>
<pre>
{
  "success": true,
  "message": "Shortened url created successfully.",
  "payload": {
    "id": 1,
    "shortId": "abc123",
    "originalUrl": "https://example.com"
  }
}
</pre>

<hr>

<h3>2.2 - GET <code>/shortener/{shortId}</code> – Retrieve original URL</h3>
<p>Returns the original URL mapped to the provided shortId.</p>

<pre>
GET /shortener/abc123
</pre>

<h4>Response Example</h4>
<pre>
{
  "success": true,
  "payload": {
    "shortId": "abc123",
    "originalUrl": "https://example.com"
  }
}
</pre>

<hr>

<h2>3 - Database Configuration</h2>

<h3>Production-like Environment</h3>
<ul>
  <li>MySQL database</li>
  <li>Flyway migrations executed at startup</li>
</ul>

<h3>Development & Test Environment</h3>
<ul>
  <li>H2 in-memory database</li>
  <li>Flyway enabled for automatic migrations</li>
  <li>Fast isolated environment for testing</li>
</ul>

<p>Environment separation handled via:</p>
<pre>
src/main/resources/
 ├─ application.yaml
 ├─ application-dev.yaml
 ├─ application-test.yaml
 └─ application-prod.yaml
</pre>

<hr>

<h2>4 - Monitoring & Metrics</h2>

<p>The application integrates Spring Boot Actuator and Prometheus to track metrics and health.</p>

<table>
  <tr><th>Feature</th><th>URL</th></tr>
  <tr><td>Actuator Dashboard</td><td><a href="http://localhost:8081/actuator/">http://localhost:8081/actuator</a></td></tr>
  <tr><td>Prometheus Metrics</td><td><a href="http://localhost:8081/actuator/prometheus">http://localhost:8081/actuator/prometheus</a></td></tr>
  <tr><td>Swagger Documentation</td><td><a href="http://localhost:8081/swagger-ui/index.html">http://localhost:8081/swagger-ui/index.html</a></td></tr>
</table>

<p>Metrics include:</p>
<ul>
  <li>HTTP request counts</li>
  <li>Response times</li>
  <li>Error rates</li>
  <li>JVM statistics</li>
  <li>Database connection metrics</li>
</ul>

<hr>

<h2>5 - Project Structure</h2>

<pre>
src/
 └── main/
     ├── java/it.fdg.shortener/
     │     ├── controllers/
     │     ├── dtos/
     │     ├── entities/
     │     ├── exceptions/
     │     ├── repositories/
     │     ├── requests/
     │     ├── services/
     │     ├── utilities/
     │     └── ShortenerApplication.java
     └── resources/
           ├── db/migration/
           ├── application.yaml
           ├── application-dev.yaml
           ├── application-test.yaml
           └── application-prod.yaml

</pre>

<hr>

<h2>6 - Technologies Used</h2>
<ul>
  <li>Java 21</li>
  <li>Spring Boot 3</li>
  <li>Spring Web</li>
  <li>Spring Data JPA</li>
  <li>Spring Actuator</li>
  <li>Prometheus</li>
  <li>Flyway</li>
  <li>H2 / MySQL</li>
  <li>Swagger (springdoc-openapi)</li>
  <li>Lombok</li>
  <li>JUnit 5 + Mockito</li>
</ul>