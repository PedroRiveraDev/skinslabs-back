# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Descargar dependencias (capa cacheable)
RUN mvn dependency:go-offline -B

# Copiar código fuente y configuración
COPY src ./src
COPY src/main/resources/application.properties ./src/main/resources/application.properties

# Construir la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine

# Instalar dependencias del sistema
RUN apk add --no-cache curl

# Crear usuario no-root
RUN addgroup -g 1001 -S spring && adduser -S spring -G spring

# Establecer directorio de trabajo
WORKDIR /app

# Copiar JAR desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Copiar configuración de Spring
COPY src/main/resources/application.properties /app/application.properties

# Crear directorio para uploads y asignar permisos
RUN mkdir -p /app/uploads && chown -R spring:spring /app

# Cambiar al usuario no-root
USER spring

# Exponer puerto
EXPOSE 8080

# Variables de entorno para JVM
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.config.location=file:/app/application.properties"]
