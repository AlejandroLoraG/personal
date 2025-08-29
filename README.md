# Route Optimization API

A high-performance Java Spring Boot REST API for calculating optimal routes between locations using Dijkstra's shortest path algorithm.

## Features

- **CSV Data Upload**: Load travel time data via semicolon-delimited CSV files
- **Route Optimization**: Calculate shortest paths between locations using graph algorithms
- **High Performance**: Sub-millisecond response times, handles 10K+ routes efficiently
- **RESTful API**: Clean JSON endpoints with proper HTTP status codes
- **Docker Ready**: Containerized deployment with health checks
- **Test Coverage**: Comprehensive test suite following TDD principles

## API Endpoints

### Upload Travel Data
```bash
POST /load-data
Content-Type: multipart/form-data

# Upload CSV file with format: location_from;location_to;time_minutes
curl -X POST -F "file=@travel_data.csv" http://localhost:8080/load-data
```

**Response:**
```json
{
  "message": "Data loaded successfully",
  "recordsProcessed": 1500
}
```

### Calculate Route
```bash
GET /route?from={location}&to={destination}

# Example
curl "http://localhost:8080/route?from=R11&to=CP5"
```

**Success Response (200):**
```json
{
  "ruta": ["R11", "TH4", "CP5"],
  "tiempoTotal": 18
}
```

**No Route Found (404):**
```json
{
  "error": "No route found between R11 and CP99"
}
```

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.6+
- Docker (optional)

### Run Locally
```bash
# Clone and build
git clone <repository-url>
cd javajava
mvn clean install

# Run application
mvn spring-boot:run

# Application available at http://localhost:8080
```

### Run with Docker
```bash
# Build and run with docker-compose
docker-compose up --build

# Or manually
docker build -t route-optimizer .
docker run -p 8080:8080 route-optimizer
```

## CSV Data Format

Upload travel time data using semicolon-delimited CSV format:

```csv
R11;R12;20
R12;R13;9
R13;R20;11
CP1;CP2;7
CP2;R20;67
```

- **Column 1**: Origin location
- **Column 2**: Destination location  
- **Column 3**: Travel time in minutes

## Architecture

- **Spring Boot 3.2**: REST API framework
- **Graph Data Structure**: Adjacency list representation
- **Dijkstra's Algorithm**: Optimal pathfinding
- **In-Memory Storage**: Fast data access
- **Maven**: Build and dependency management

## Performance

- **Response Time**: <1ms for route calculations
- **Scalability**: Tested with 10,000+ route network
- **Memory Efficient**: Optimized graph representation
- **Docker Ready**: Alpine Linux base for minimal footprint

## Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=RouteControllerTest

# Generate test data
python generate_test_data.py
```

## Development

Built using Test-Driven Development (TDD) with comprehensive test coverage:

- **Unit Tests**: Individual component testing
- **Integration Tests**: End-to-end workflow testing
- **Performance Tests**: Response time validation

## Contributing

1. Fork the repository
2. Create a feature branch
3. Write tests following TDD approach
4. Implement functionality
5. Ensure all tests pass
6. Submit a pull request

## License

This project is licensed under the MIT License.