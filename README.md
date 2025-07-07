# 🛒 DSCommerce
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://github.com/Luis-Parente/DSCommerce/blob/main/LICENSE)

## Description
DSCommerce is a backend REST API for an e-commerce platform, developed using Java 21 and Spring Boot 3.2.5. It implements a layered architecture with domain modeling via JPA, and an embedded H2 database for persistence.

## 📋 Features
- Product and category management
- User registration
- Order processing
- Role-based authorization
- Exception handling
- DTO pattern and data validation

## ✅ Requirements
- Java 21+
- Git

## 🛠️ Installation & Execution
### 1.Clone the repository:
````bash
git clone https://github.com/YOUR_USERNAME/dscommerce.git
cd dscommerce/dscommerce
````
### 2.Build the project using the Maven Wrapper:
````bash
./mvnw clean install
````
### 3.Run the application with:
````bash
./mvnw spring-boot:run
````
### Once running, the following resources will be available:

- API Base URL: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
- Swagger UI: http://localhost:8080/swagger-ui/index.html

## 🔍 Testing the API
You can test the API in two ways:

### 1.Swagger UI
Accessible at http://localhost:8080/swagger-ui/index.html. It provides a full list of available endpoints with detailed request/response schemas and example payloads.

### 2.Postman
Use Postman for a more flexible API testing experience.

The repository includes:

- A Postman collection (DSCommerce.postman_collection.json)

- A Postman environment file (DSCommerce Auth.postman_environment.json)

💾 Import both files into Postman to get started immediately:

- Go to File > Import
- Select the .json files from the repository
- Use the environment to auto-fill base URLs and variables
- You can then send requests directly using the pre-configured endpoints and data.

## 🧰 Tech Stack
- Java 21
- Spring Boot 3.2.5
- Spring Web
- Spring Data JPA
- H2 Database (embedded)
- Maven Wrapper
- Swagger / OpenAPI
- Postman (testing)
- JUnit (testing)
- Git
