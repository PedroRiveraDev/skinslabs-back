# SkinsLabs Backend

Backend en Java (Spring Boot 3 + GraphQL + JPA) para catálogo de bots de automatización.

## Arquitectura
- **Modelo principal:** `BotServicio` con relaciones a `Funcion`, `Integracion`, `CasoUso`, `Tecnologia`, `FlujoAutomatizado`, `Requisito`.
- **Persistencia:** JPA/Hibernate, base de datos en memoria H2.
- **API:** GraphQL (`/graphql`), esquema en `src/main/resources/graphql-client/schema.graphqls`.
- **Capas:**
  - `model`: entidades JPA
  - `repository`: repositorios Spring Data JPA
  - `service`: lógica de negocio y relaciones
  - `controller`: resolvers GraphQL

## Comandos útiles

### Compilar el proyecto
```bash
mvn clean install -DskipTests
```

### Ejecutar la aplicación
```bash
mvn spring-boot:run
```

### Acceder a la consola H2
- URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:skinslabsdb`
- Usuario: `sa` (sin contraseña)

### Acceder a GraphQL Playground
- URL: [http://localhost:8080/graphiql](http://localhost:8080/graphiql) (si tienes GraphiQL habilitado)
- Endpoint: `/graphql`

## Escalabilidad
- Listo para agregar autenticación, paginación, validaciones y más relaciones.
- Código limpio, comentado y siguiendo buenas prácticas.

## Notas
- Todos los datos se almacenan en la base H2 en memoria (ideal para pruebas/desarrollo).
- No hay datos hardcodeados: todo se gestiona vía GraphQL y la base de datos.

## Validaciones y manejo de errores

- Todos los campos importantes tienen validaciones (obligatorio, longitud mínima/máxima, etc.).
- Si envías datos inválidos, recibirás un error claro en la respuesta GraphQL, por ejemplo:

```json
{
  "errors": [
    {
      "message": "Error de validación: El título es obligatorio"
    }
  ],
  "data": null
}
```

- El backend implementa un manejador global de errores para que cualquier excepción o error de validación se devuelva de forma clara y amigable.

## Buenas prácticas
- Código documentado con JavaDoc y comentarios.
- Separación por capas (model, repository, service, controller).
- Validaciones centralizadas y automáticas.
- Manejo global de errores para GraphQL.

## Subida y gestión de imágenes de bots

Cada bot puede tener asociada una imagen. Para ello:

- Usa el endpoint REST para subir la imagen:

```
POST /api/bots/{id}/imagen
Content-Type: multipart/form-data
file: <archivo_imagen>
```

- El backend guarda la imagen en la carpeta `/uploads` y actualiza el campo `imagenUrl` del bot.
- El campo `imagenUrl` está disponible en las queries y mutaciones de GraphQL.
- Puedes acceder a la imagen desde el frontend usando la URL devuelta (por ejemplo: `http://<host>/uploads/bot_1_...jpg`).

### Ejemplo de mutación GraphQL para crear un bot con imagen

```graphql
mutation {
  crearBot(input: {
    titulo: "Mi Bot"
    descripcion: "Un bot de ejemplo"
    imagenUrl: "/uploads/bot_1_123456.jpg" # tras subir la imagen
    funciones: []
    integraciones: []
    casosUso: []
    tecnologias: []
    flujosAutomatizados: []
    requisitos: []
  }) {
    id
    titulo
    imagenUrl
  }
}
``` 