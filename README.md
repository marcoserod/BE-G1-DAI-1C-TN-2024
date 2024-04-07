# MoviePlay API

La API MoviePlay ofrece servicios para la gestión de películas y usuarios, permitiendo a los usuarios acceder a información detallada, buscar, filtrar, calificar y marcar películas como favoritas. Desplegada en la nube con documentación Swagger, garantiza una experiencia fluida y escalabilidad. Desarrollada con Node.js/Express o Spring Boot, ofrece flexibilidad y se integra perfectamente con la aplicación móvil MoviePlay.

## Índice

- [Descripción](#descripción)
- [Tecnologías](#tecnologías)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Documentación](#documentación)


## Descripción

La API MoviePlay ofrece una plataforma integral para gestionar películas y usuarios, desplegada en la nube y diseñada para ser flexible e integrarse fácilmente, siendo la base de la experiencia inmersiva y personalizada en la aplicación móvil MoviePlay.

## Tecnologías

- **Backend**: Java Spring Boot
- **Base de datos**: MySql
- **Despliegue**: Google Cloud Platform
- **Documentación**: Swagger

## Instalación

Sigue estos pasos para configurar y ejecutar el proyecto en un entorno local:

1. **Clonar el repositorio:**
```
git clone https://github.com/marcoserod/BE-G1-DAI-1C-TN-2024.git
```
2. **Navegar al directorio del proyecto:**
```
cd BE-G1-DAI-1C-TN-2024
```
3. **Compilar el proyecto:**
```
mvn clean install
```
4. **Ejecutar la aplicación:**
```
mvn spring-boot:run
```


## Configuración
### Base de datos - Mysql
1. Descargar e instalar el servidor de base de datos: MySQL.

     ```
     Link: https://dev.mysql.com/downloads/mysql/
     ```

2. Abrir el CMD e ir a la ubicación de la carpeta /bin *(creada con la instalación del server.)* con el siguiente comando:

    ```cmd 
    cd C:\Program Files\MySQL\MySQL Server 8.1\bin
    ```

3. Paso 4: Inicia el servidor MySQL.

    ```
    mysqld
    ```
4. Una vez que el servidor MySQL esté levantado, resta conectarse usando el cliente MySQL con el siguiente comando. (En la consola mysql)
    ```
    mysql -u root -p
    ```
   Se va a aparecer el siguiente mensaje:
    ```
    Enter password:
    ```
   Esto te pedirá la contraseña del usuario root de MySQL. Ingrésala y presiona Enter.

   ***A esta altura ya estamos interactuando directamente con la Linea de comandos de la base de datos.***
5. Verificar bases de datos existentes:
   ```sql
   mysql> SHOW DATABASES;
   ```
6. Vamos a crear la bbdd "DAIDB":
   ```sql
   mysql> CREATE DATABASE DAIDB;
   ```
7. Luego, selecciona la base de datos "DAIDB" con el siguiente comando:
   ```sql
   mysql> USE DAIDB;
   ```
8. Ahora estás listo para crear la tabla "Usuarios". Correr el siguiente script en la misma linea de comandos:

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
9. Luego, creamos la tabla "user_favorites":

   ```sql
   mysql> CREATE TABLE user_favorites (
               id BIGINT AUTO_INCREMENT PRIMARY KEY,
               user_id INT,
               FOREIGN KEY (user_id) REFERENCES users(id),
               film_id BIGINT
            );
   ```

10. Vamos a validar que se hayan creado correctamente con el comando:

    ```sql
    mysql> SHOW TABLES;
    ```

## Documentación
### Api swagger
Se puede visualizar el swagger con las operaciones que expone la api en la siguiente ruta: [ Swagger Link ](http://localhost:8080/swagger-ui/index.html "Swagger")
