# Etapa 1: construir el proyecto con Maven
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos primero el pom.xml para descargar dependencias
COPY pom.xml .

# Descarga dependencias antes de copiar el código
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Construimos el JAR
RUN mvn clean package -DskipTests

# Etapa 2: imagen liviana para ejecutar la app
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiamos el JAR generado desde la etapa build
COPY --from=build /app/target/*.jar app.jar

# Puerto del gateway
EXPOSE 8081

# Ejecutar aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]