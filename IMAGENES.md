# Funcionalidad de Imágenes - SkinsLabs Backend

## Descripción

El backend de SkinsLabs ahora incluye una funcionalidad completa para manejar imágenes de bots. Las imágenes se almacenan en la carpeta `/uploads` y se sirven a través de endpoints REST y recursos estáticos.

## Características

- ✅ Subida de imágenes para bots específicos
- ✅ Validación de tipos de archivo (JPEG, JPG, PNG, GIF, WEBP)
- ✅ Validación de tamaño (máximo 10MB)
- ✅ Nombres únicos para evitar conflictos
- ✅ Eliminación de imágenes
- ✅ Servido de imágenes con cache
- ✅ Configuración CORS para frontend
- ✅ Validaciones de seguridad

## Endpoints Disponibles

### 1. Subir Imagen para un Bot

```http
POST /api/bots/{id}/imagen
Content-Type: multipart/form-data

file: <archivo_imagen>
```

**Respuesta exitosa:**
```json
"/uploads/bot_1_1234567890.jpg"
```

**Errores posibles:**
- `404 Not Found`: Bot no encontrado
- `400 Bad Request`: Archivo inválido o muy grande
- `500 Internal Server Error`: Error al guardar

### 2. Eliminar Imagen de un Bot

```http
DELETE /api/bots/{id}/imagen
```

**Respuesta exitosa:**
```json
"Imagen eliminada correctamente"
```

### 3. Servir Imagen

```http
GET /uploads/{filename}
```

**Headers incluidos:**
- `Content-Type`: Tipo de imagen apropiado
- `Cache-Control`: `public, max-age=31536000` (1 año)
- `Access-Control-Allow-Origin`: `*`

### 4. Verificar Existencia de Imagen

```http
GET /api/imagenes/existe/{filename}
```

**Respuesta:**
```json
true/false
```

## Uso desde Frontend

### Subir Imagen

```javascript
const formData = new FormData();
formData.append('file', imagenSeleccionada);

const response = await fetch(`http://localhost:8080/api/bots/${botId}/imagen`, {
    method: 'POST',
    body: formData
});

if (response.ok) {
    const imagenUrl = await response.text();
    console.log('Imagen subida:', imagenUrl);
    // imagenUrl será algo como: "/uploads/bot_1_1234567890.jpg"
}
```

### Mostrar Imagen

```javascript
// Usando la URL completa
const imagenUrl = `http://localhost:8080${bot.imagenUrl}`;
<img src={imagenUrl} alt="Imagen del bot" />

// O directamente si el frontend está configurado para proxy
<img src={bot.imagenUrl} alt="Imagen del bot" />
```

### Eliminar Imagen

```javascript
const response = await fetch(`http://localhost:8080/api/bots/${botId}/imagen`, {
    method: 'DELETE'
});

if (response.ok) {
    console.log('Imagen eliminada correctamente');
}
```

## Configuración

### application.properties

```properties
# Configuración para subida de archivos
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Configuración para servir archivos estáticos
spring.web.resources.static-locations=classpath:/static/,file:uploads/
spring.web.resources.add-mappings=true
```

### CORS

El backend está configurado para permitir peticiones desde `http://localhost:3000` (frontend típico de React/Vue).

## Estructura de Archivos

```
uploads/
├── bot_1_1234567890.jpg
├── bot_2_1234567891.png
└── bot_3_1234567892.gif
```

## Validaciones

### Tipos de Archivo Permitidos
- JPEG (.jpg, .jpeg)
- PNG (.png)
- GIF (.gif)
- WEBP (.webp)

### Tamaño Máximo
- 10MB por archivo

### Seguridad
- Validación de nombres de archivo para evitar path traversal
- Verificación de que los archivos están dentro del directorio uploads
- Headers de seguridad apropiados

## GraphQL

El campo `imagenUrl` está disponible en todas las queries y mutaciones de GraphQL:

```graphql
query {
  obtenerBots {
    id
    titulo
    descripcion
    imagenUrl
  }
}
```

## Troubleshooting

### La imagen no se muestra
1. Verifica que la URL de la imagen sea correcta
2. Asegúrate de que el archivo existe en la carpeta `/uploads`
3. Revisa la consola del navegador para errores CORS

### Error al subir imagen
1. Verifica que el archivo sea una imagen válida
2. Asegúrate de que el tamaño sea menor a 10MB
3. Verifica que el bot existe en la base de datos

### Error de CORS
1. Asegúrate de que el frontend esté corriendo en `http://localhost:3000`
2. Verifica que la configuración CORS esté correcta

## Notas de Desarrollo

- Las imágenes se almacenan con nombres únicos basados en timestamp
- El directorio `/uploads` se crea automáticamente si no existe
- Las imágenes se sirven con cache de 1 año para mejor rendimiento
- Todos los endpoints incluyen manejo de errores apropiado 