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
- **Database**: MySql
- **Deployment**: Google Cloud Platform
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


## Configuration
### Database - Mysql
1. Download and install a database server: MySQL.

     ```
     Link: https://dev.mysql.com/downloads/mysql/
     ```

2. Open the Command Prompt (CMD) and navigate to the location of the /bin folder (created during the server installation) 
using the following command

    ```cmd 
    cd C:\Program Files\MySQL\MySQL Server 8.1\bin
    ```

3. Start the MySQL server

    ```
    mysqld
    ```
4. Once the MySQL server is up and running, you can connect using the MySQL client with the following command. 
(In the mysql console)
    ```
    mysql -u root -p
    ```
   You will see the following message
    ```
    Enter password:
    ```
   This will prompt you to enter the password for the MySQL root user. Enter it and press Enter.

   *** At this point, we are already interacting directly with the database command line.***
5. Verify existing databases:
   ```sql
   mysql> SHOW DATABASES;
   ```
6. Create database named "DAIDB":
   ```sql
   mysql> CREATE DATABASE DAIDB;
   ```
7. Then, select the "DAIDB" database with the following command:
   ```sql
   mysql> USE DAIDB;
   ```
8. Now you are ready to create the "Usuarios" table. Run the following script in the same command line:

   ```sql
   mysql> CREATE TABLE users (
               id INT AUTO_INCREMENT PRIMARY KEY,
               email VARCHAR(255),
               name VARCHAR(255),
               surname VARCHAR(255),
               nickname VARCHAR(255),
               profile_image VARCHAR(255)
            );
   ```
9. Then, create table "user_favorites":

   ```sql
   mysql> CREATE TABLE user_favorites (
               id BIGINT AUTO_INCREMENT PRIMARY KEY,
               user_id INT,
               FOREIGN KEY (user_id) REFERENCES users(id),
               film_id BIGINT
            );
   ```

10. Let's validate that they have been created correctly with the command:

    ```sql
    mysql> SHOW TABLES;
    ```

## Documentation
### Api swagger
The Swagger documentation with the exposed API operations can be viewed at the following path:
[ Swagger Link ](https://app.swaggerhub.com/apis/Grupo_01/Grupo_01_DAI/1.1.5 "Swagger")
