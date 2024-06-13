# MoviePlay API

The MoviePlay API offers services for managing movies and users, allowing users to access detailed information, 
search, filter, rate, and mark movies as favorites. Deployed in the cloud with Swagger documentation, it ensures a 
smooth experience and scalability. Developed with Spring Boot, it offers flexibility and seamlessly integrates with 
the MoviePlay mobile application.

## Index

- [Description](#description)
- [Technologies](#technologies)
- [Installation](#installation)
- [Configuration](#configuration)
- [Documentation](#documentation)


## Description

The MoviePlay API offers a comprehensive platform for managing movies and users, deployed in the cloud and designed 
to be flexible and easily integrated, serving as the foundation for the immersive and personalized experience in 
the MoviePlay mobile application.

## Technologies

- **Backend**: Java Spring Boot
- **Database**: PostgreSQL
- **Deployment**: Render
- **Documentation**: Swagger

## Installation

Follow these steps to configure and run the project in a local environment:

1. **To clone the repository:**
```
git clone https://github.com/marcoserod/BE-G1-DAI-1C-TN-2024.git
```
2. **Navigate to the project directory:**
```
cd BE-G1-DAI-1C-TN-2024
```
3. **Compile the project:**
```
mvn clean install
```
4. **Run the application:**
```
mvn spring-boot:run
```


### Pre-Deployment Steps

Before deploying new changes or updating the repository, ensure the Docker image is up-to-date running the following 
command:
```
mvn clean package 
```


## Configuration
### Database - Postgresql on Render
This section provides information on how to work with the configured PostgreSQL database on Render for 
our application.
#### Configuration Overview
Database connection settings are already configured in the application.properties file of the application. 
This file includes the necessary credentials and connection information required to connect to the PostgreSQL 
database hosted on Render when running application.

#### Connecting to the Database with client
1. To manage the database schema or perform any database operations, you can connect to PostgreSQL database using any 
PostgreSQL client tool that you prefer, such as psql or a GUI-based tool like pgAdmin. You need to ensure PostgreSQL is 
installed on your machine. Here's how to proceed:

     ```
     Link: https://www.postgresql.org/download/
     ```

2. Open the Command Prompt (CMD) and navigate to the location of the /bin folder (created during the server installation) 
using the following command. 

    ```
     cd C:\Program Files\PostgreSQL\16\bin
    ```
3. To simplify the usage of PostgreSQL commands from any Command Prompt window, you should add
   the PostgreSQL bin directory to your system's PATH environment variable. 

4. Connect using the PostgreSQL client or with the following command on cmd. 

    ```
    psql -h <hostname> -U <username> -d <dbname>
    ```
   You will see the following message
    ```
    Enter password:
    ```
   This will prompt you to enter the password for the PostgreSQL server. Enter it and press Enter.

5. Now you are ready to see the created tables and perform additional operations as needed

   ```
   psql> \dt;
   ```

## Documentation
### Api swagger
The Swagger documentation with the exposed API operations can be viewed at the following path:
[ Swagger Link ](https://app.swaggerhub.com/apis/Grupo_01/Grupo_01_DAI/2.0.7 "Swagger")
