# Insurance API

## Introduction

Insurance API is an application that allows the insurance team
to manage and retrieve information related to customers
vehicle insurance for support and business decisions.
The product is built using Java Spring Boot to create the API
endpoints, Maven for dependencies, Swagger for API
documentation, MongoDB to store the data, and Git for version
control.

## Quick Start

### 1. Start Your Database
Start your MongoDB Database

Ensure your application properties file contains your database
information (host, port, and database):
```
insurance-mongo/src/main/resources/applicatioin.properties
```
### 2. Start the API
Build your project with Maven:
```
mvn clean package
```
Run executable JAR:
```
java -jar target/insurance-mongo-0.0.1-SNAPSHOT.jar
```
### 3. Use Swagger to see API Documentation
Access Swagger:
```
http://localhost:8080/swagger-ui/index.html
```
## Implementation
- Insurance API was created by using Java with the 
Spring framework using controllers, models,
repositories, and services
- The `Models` package contains classes for defining the
structure of data entities
- The `Controller` package contains the classes for request
handling and processing
- The `Repository` package contains the classes for
aggregation queries
- The `Service` package contains classes for encapsulating
the business logic like CRUD operations
- The data  was stored in MongoDB
- The `Aggregation` package contains all the classes which
are used to hold the result of the aggregation queries
in `PersonRepository`
- Maven was used for dependency management
- Swagger was used for API documentation

### Architecture

## Testing
- Swagger was used as documentation to help with 
testing out the API calls
- Postman was used for testing the API calls
- Displaying the Person data when creating and deleting
information, so you are confident the API call was 
successful

## Deployment
The app was deployed using Java Spring and MongoDB

- Git was used for local version control
- Github was used to host the Git's repo online

## Improvements
- A front-end application to make the user experience
for the product more friendly
- Authentication and Authorization for specific queries
like deleting data
- Analytic dashboard for database information like
how many insured people are adults vs minors
