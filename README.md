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
### Base de datos

La aplicación utiliza una base de datos para almacenar información sobre películas y usuarios. Sigue estos pasos para configurar la base de datos:

1. **Configuración del archivo application.properties:**

   Abre el archivo `application.properties` en la carpeta `src/main/resources` y configura los detalles de conexión a tu base de datos. Por ejemplo:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/moviedb
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   ```
2. **Creación de la base de datos:**

   Crea una base de datos en tu sistema de gestión de bases de datos utilizando el nombre especificado en la propiedad `spring.datasource.url`.

## Documentación
### Api swagger
Se puede visualizar el swagger con las operaciones que expone la api en la siguiente ruta: [ Swagger Link ](http://localhost:8080/swagger-ui/index.html "Swagger")
