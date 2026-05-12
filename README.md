# Banking App

A Spring Boot banking service module for account creation and transfers, using JPA with an embedded H2 database.

## Project Structure

- `banking-core/` - main Spring Boot module
- `banking-core/src/main/java/com/bank/app/` - application source code
- `banking-core/src/main/resources/application.properties` - runtime configuration

## Technology Stack

- Java 21
- Spring Boot 3.2
- Spring Web
- Spring Data JPA
- H2 in-memory database
- Maven build

## Requirements

- Java 21 JDK
- Maven 3.8+

## Build and Run

From the repository root:

```bash
cd banking-core
mvn clean package
mvn spring-boot:run
```

Or run the generated JAR:

```bash
java -jar target/banking-core-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### Create account

- URL: `POST /api/accounts`
- Body:
  ```json
  {
    "initialBalance": 1000.0
  }
  ```
- Response:
  ```json
  {
    "accountId": 1,
    "accountNumber": "ACC-1"
  }
  ```

### Get account

- URL: `GET /api/accounts/{accountId}`
- Response:
  ```json
  {
    "id": 1,
    "accountNumber": "ACC-1",
    "balance": 1000.0
  }
  ```

### Transfer funds

- URL: `POST /api/transfers`
- Body:
  ```json
  {
    "fromAccountId": 1,
    "toAccountId": 2,
    "amount": 100.0
  }
  ```
- Response:
  ```text
  Transfer processed
  ```

## Database

The application uses an in-memory H2 database configured in `banking-core/src/main/resources/application.properties`.

- JDBC URL: `jdbc:h2:mem:bankdb`
- H2 console is enabled at `/h2-console`

## Notes

- The app uses `spring.jpa.hibernate.ddl-auto=update` so JPA creates and updates tables automatically.
- Logs are written to `logs/banking-app.log` as configured in `application.properties`.
