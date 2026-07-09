# Evaluación 3 - Microservicios Hotel Veranum

## Descripción del Proyecto
Este proyecto es una arquitectura de microservicios desarrollada en Java con Spring Boot para la gestión integral de un hotel. El sistema divide la lógica de negocio en servicios independientes para la Gestión de Usuarios y la Gestión de Reservas, cada uno con su propia base de datos MySQL para asegurar la persistencia y aislamiento de los datos. Todo el ecosistema está unificado bajo un API Gateway y empaquetado en contenedores utilizando Docker y Docker Compose para garantizar un despliegue rápido y estandarizado.

## Integrantes del Equipo
* Angelo Campillay Poblete
* Brayan Campillay Poblete
* Victor Cortez

---

## Aporte realizado por cada integrante

### Angelo Campillay - Arquitectura API y Documentación
* Documentación de Controladores: Implementación de la especificación OpenAPI/Swagger mediante anotaciones en la capa web, asegurando la correcta exposición de los endpoints (GET, POST, PUT, DELETE).
* Configuración del API Gateway: Unificación de los microservicios bajo un único puerto de entrada y configuración de las reglas de enrutamiento (routes y predicates).
* Gestión de Entornos: Transición de los archivos de configuración a formato YAML y creación de los perfiles de ejecución (dev y prod) para el manejo dinámico de variables.

### Brayan Campillay - Infraestructura, Docker y Backend
* Contenerización: Creación y configuración de los archivos Dockerfile en la raíz de cada microservicio para la generación de las imágenes aisladas del sistema.
* Control de Versiones: Administración de las ramas en Git/GitHub, resolución de conflictos de fusión (merge conflicts) y empaquetado final de la versión a entregar.
* Ajustes de Lógica para Despliegue: Modificación de la capa de Servicios y actualización de dependencias en los archivos pom.xml para asegurar que el código Java sea compatible con la arquitectura de contenedores.

### Victor Cortez - Testing y Aseguramiento de Calidad
* Pruebas de Integración: Ejecución de Testing sobre los microservicios para garantizar que la comunicación interna (Feign Client/WebClient) y la persistencia de datos funcionen correctamente.
* Verificación de Compilación: Ejecución del ciclo de vida de Maven (Clean, Compile, Package) asegurando que los ejecutables .jar se generen sin errores.
* Validación de la API: Testeo de la interfaz interactiva de Swagger, comprobando que los códigos de estado HTTP (200, 201, 400, 404, 500) coincidan con el comportamiento real del sistema ante diferentes peticiones.

> Nota: A pesar de la división de tareas principales, el equipo realizó reuniones de revisión cruzada. Todos los integrantes conocen la estructura general y están capacitados para realizar ajustes en la lógica de negocio, configuración o infraestructura en caso de nuevos requerimientos.

---

## Puertos y Rutas del API Gateway
El API Gateway funciona como el punto de entrada principal para las peticiones externas, redirigiendo el tráfico hacia los microservicios correspondientes a través de la red interna de Docker.

| Servicio | Puerto Expuesto | Ruta en el Gateway |
| :--- | :--- | :--- |
| API Gateway | 8080 | http://localhost:8080 |
| Microservicio Usuarios | 8081 | /api/v1/usuarios/** |
| Microservicio Reservas | 8082 | /api/v1/reservas/** |
| Base de Datos Usuarios | 3306 | N/A (Uso interno) |
| Base de Datos Reservas | 3307 | N/A (Uso interno) |

---

## APIs y Endpoints Disponibles

### API Gestión de Usuarios (`/api/v1/usuarios`)
* `POST /api/v1/usuarios` - Registrar un nuevo usuario.
* `GET /api/v1/usuarios` - Listar todos los usuarios registrados en el sistema.
* `GET /api/v1/usuarios/{id}` - Buscar un usuario específico por su ID.
* `GET /api/v1/usuarios/existe/{rut}` - Validar la existencia de un usuario mediante su RUT (Endpoint de comunicación interna).
* `PUT /api/v1/usuarios/{id}` - Actualizar los datos de un usuario.
* `DELETE /api/v1/usuarios/{id}` - Eliminar un usuario del sistema.
* `PUT /api/v1/usuarios/{id}/bloquear` - Cambiar el estado de bloqueo de un usuario.

### API Gestión de Reservas (`/api/v1/reservas`)
* `POST /api/v1/reservas` - Crear una nueva reserva (Valida internamente la existencia del usuario).
* `GET /api/v1/reservas` - Listar todas las reservas registradas.
* `GET /api/v1/reservas/{id}` - Obtener el detalle de una reserva específica.
* `GET /api/v1/reservas/usuario/{rut}` - Obtener el historial de reservas asociado a un usuario específico mediante su RUT.
* `PUT /api/v1/reservas/{id}/cancelar` - Cancelar una reserva y liberar la habitación.

---

## Enlaces de Swagger
Una vez que el proyecto esté en ejecución, la documentación interactiva de la API estará disponible en las siguientes direcciones:

* Swagger Usuarios: http://localhost:8081/swagger-ui.html
* Swagger Reservas: http://localhost:8082/swagger-ui.html

---

## Instrucciones para ejecutar y probar el sistema

### Requisitos Previos
1. Tener Docker Desktop instalado y en ejecución.
2. Contar con una terminal (Git Bash, PowerShell o la terminal del IDE).

### Paso a Paso para Despliegue Local
1. Clonar el repositorio: Descargar el proyecto desde GitHub asegurándose de estar en la rama principal consolidada.
2. Generar los ejecutables: Abrir el proyecto en el IDE (ej: IntelliJ) y ejecutar el comando package de Maven en ambos microservicios para generar los archivos .jar actualizados.
3. Levantar la infraestructura: Abrir una terminal en la ruta raíz del proyecto (donde se encuentra el archivo docker-compose.yml) y ejecutar el siguiente comando:
   ```bash
   docker compose up -d --build
   ```
4. Verificación: Esperar aproximadamente 20 segundos para que las bases de datos y el servidor Tomcat interno inicien correctamente. Los contenedores deben mostrar el estado Healthy (bases de datos) y Started (microservicios).
5. Pruebas: Acceder a los enlaces de Swagger mencionados arriba y utilizar el botón Try it out para ejecutar los endpoints. Se recomienda crear primero un Usuario y utilizar su RUT para probar la creación de una Reserva.
6. Detener el sistema: Para apagar y limpiar los contenedores, ejecutar el siguiente comando en la terminal:
   ```bash
   docker compose down
   ```
