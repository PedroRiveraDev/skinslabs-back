# Testing y Análisis de Calidad - SkinsLabs Backend

Este documento describe cómo ejecutar los tests, generar reportes de cobertura y configurar SonarQube Cloud para el análisis de calidad del código.

## 📋 Configuración de Tests

### Prerrequisitos
- Java 17 o superior
- Maven 3.6 o superior
- Cuenta en SonarQube Cloud (opcional)

### Estructura de Tests

```
src/test/java/com/skinslabs/back/
├── model/
│   └── BotServicioTest.java          # Tests de validación de modelos
├── service/
│   └── BotServicioServiceTest.java    # Tests de servicios
├── controller/
│   └── BotServicioGraphQLControllerTest.java  # Tests de controladores
├── repository/
│   └── BotServicioRepositoryTest.java # Tests de repositorios
└── BackApplicationIntegrationTest.java # Tests de integración
```

## 🚀 Ejecutar Tests

### Opción 1: Usando Maven directamente

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests con cobertura
mvn test jacoco:report

# Verificar cobertura mínima (80%)
mvn jacoco:check
```

### Opción 2: Usando scripts

**Windows:**
```cmd
run-tests.bat
```

**Linux/Mac:**
```bash
./run-tests.sh
```

## 📊 Reportes de Cobertura

### JaCoCo (Java Code Coverage)

Los reportes se generan automáticamente en:
- **HTML**: `target/site/jacoco/index.html`
- **XML**: `target/site/jacoco/jacoco.xml`

### Cobertura Mínima Configurada

- **Líneas de código**: 80%
- **Ramas**: 80%
- **Funciones**: 80%

### Verificar Cobertura

```bash
# Verificar que se cumple la cobertura mínima
mvn jacoco:check

# Si falla, ver el reporte HTML para detalles
open target/site/jacoco/index.html
```

## 🔍 SonarQube Cloud

### Configuración

1. **Crear cuenta en SonarQube Cloud**
   - Ve a [sonarcloud.io](https://sonarcloud.io)
   - Crea una cuenta gratuita

2. **Crear organización**
   - Crea una organización llamada `skinslabs`

3. **Crear proyecto**
   - Crea un proyecto llamado `skinslabs-back`
   - Selecciona GitHub como proveedor de repositorio

4. **Obtener token**
   - Ve a Account → Security
   - Genera un token de análisis

### Variables de Entorno

Configura las siguientes variables de entorno:

**Windows:**
```cmd
set SONAR_TOKEN=tu_token_aqui
```

**Linux/Mac:**
```bash
export SONAR_TOKEN=tu_token_aqui
```

### Ejecutar Análisis

```bash
# Análisis completo con SonarQube
mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN
```

### Configuración del Proyecto

El archivo `sonar-project.properties` contiene la configuración:

```properties
sonar.projectKey=PedroRiveraDev_skinslabs-back
sonar.organization=pedroriveradev
sonar.host.url=https://sonarcloud.io
sonar.sources=src/main/java
sonar.tests=src/test/java
sonar.java.binaries=target/classes
sonar.java.test.binaries=target/test-classes
sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
```

## 🧪 Tipos de Tests

### 1. Tests Unitarios
- **Modelos**: Validación de entidades y restricciones
- **Servicios**: Lógica de negocio
- **Controladores**: Endpoints GraphQL
- **Repositorios**: Operaciones de base de datos

### 2. Tests de Integración
- **BackApplicationIntegrationTest**: Flujos completos de la aplicación
- **Repository Tests**: Tests con base de datos H2 en memoria

### 3. Tests de Validación
- Validación de restricciones JPA
- Validación de anotaciones Bean Validation
- Tests de relaciones entre entidades

## 📈 Métricas de Calidad

### Cobertura de Código
- **Líneas**: Porcentaje de líneas ejecutadas
- **Branches**: Porcentaje de ramas cubiertas
- **Functions**: Porcentaje de funciones ejecutadas

### Calidad del Código (SonarQube)
- **Bugs**: Errores potenciales
- **Vulnerabilities**: Vulnerabilidades de seguridad
- **Code Smells**: Problemas de mantenibilidad
- **Duplications**: Código duplicado
- **Technical Debt**: Deuda técnica

## 🔧 Troubleshooting

### Problemas Comunes

1. **Tests fallan por validación**
   ```bash
   # Verificar que las entidades tienen las anotaciones correctas
   mvn test -Dtest=BotServicioTest
   ```

2. **Cobertura insuficiente**
   ```bash
   # Ver reporte detallado
   open target/site/jacoco/index.html
   ```

3. **SonarQube no encuentra archivos**
   ```bash
   # Verificar configuración
   cat sonar-project.properties
   ```

4. **Token de SonarQube inválido**
   ```bash
   # Regenerar token en SonarCloud
   # Actualizar variable de entorno
   ```

### Logs de Debug

```bash
# Ejecutar con logs detallados
mvn test -X

# Ver logs de SonarQube
mvn sonar:sonar -X
```

## 📚 Recursos Adicionales

- [Documentación JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/)
- [SonarQube Cloud Documentation](https://docs.sonarqube.org/latest/)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

## 🎯 Próximos Pasos

1. **Mejorar cobertura**: Agregar tests para casos edge
2. **Performance tests**: Agregar tests de rendimiento
3. **Security tests**: Agregar tests de seguridad
4. **API tests**: Agregar tests de endpoints REST/GraphQL
5. **Database tests**: Agregar tests de migraciones

---

**Nota**: Este proyecto está configurado para mantener una cobertura mínima del 80% y seguir las mejores prácticas de calidad de código definidas por SonarQube. 