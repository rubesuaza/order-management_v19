# Management Project

Spring Boot application following Hexagonal Architecture (Ports and Adapters) pattern.

## Project Structure

```
src/main/java/com/example/management/
├── application/                # Application Layer
│   ├── ports/                 # Ports (Interfaces)
│   │   ├── in/                # Input Ports (Use Cases interfaces)
│   │   └── out/               # Output Ports (Repository/External interfaces)
│   └── services/              # Use Case Implementations
├── domain/                     # Domain Layer (Pure Logic)
│   ├── model/                 # Domain Entities/VOs
│   └── exception/             # Domain Exceptions
├── infrastructure/             # Infrastructure Layer (Adapters)
│   ├── adapters/
│   │   ├── in/                # Input Adapters (Web/REST/Controllers)
│   │   └── out/               # Output Adapters (Persistence/External APIs)
│   └── config/                # Framework-specific configuration
└── ManagementApplication.java  # Main Entry Point
```

## Building the Project

```bash
./gradlew build
```

## Running the Application

```bash
./gradlew bootRun
```

## Testing

```bash
./gradlew test
```
