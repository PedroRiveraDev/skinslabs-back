package com.skinslabs.back.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ImagenControllerTest {

    @TempDir
    Path tempDir;

    private ImagenController imagenController;
    private Path uploadsDir;

    @BeforeEach
    void setUp() throws IOException {
        // Crear directorio uploads temporal
        uploadsDir = tempDir.resolve("uploads");
        Files.createDirectories(uploadsDir);
        
        // Cambiar el directorio de trabajo temporalmente
        System.setProperty("user.dir", tempDir.toString());
        
        imagenController = new ImagenController();
    }

    @Test
    void testServirImagen_Exito() throws IOException {
        // Crear archivo de imagen de prueba
        String filename = "test.jpg";
        Path imagePath = uploadsDir.resolve(filename);
        Files.write(imagePath, "fake image content".getBytes());

        ResponseEntity<org.springframework.core.io.Resource> response = 
            imagenController.servirImagen(filename);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("image/jpeg", response.getHeaders().getContentType().toString());
    }

    @Test
    void testServirImagen_ArchivoNoEncontrado() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            imagenController.servirImagen("nonexistent.jpg");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Imagen no encontrada", exception.getReason());
    }

    @Test
    void testServirImagen_NombreArchivoInvalido() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            imagenController.servirImagen("../malicious.jpg");
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Nombre de archivo inválido", exception.getReason());
    }

    @Test
    void testServirImagen_PathTraversal() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            imagenController.servirImagen("../../../etc/passwd");
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testServirImagen_DiferentesFormatos() throws IOException {
        // Probar diferentes formatos de imagen
        String[] formats = {"test.png", "test.gif", "test.webp", "test.bmp", "test.svg"};
        String[] expectedTypes = {
            "image/png", "image/gif", "image/webp", "image/bmp", "image/svg+xml"
        };

        for (int i = 0; i < formats.length; i++) {
            Path imagePath = uploadsDir.resolve(formats[i]);
            Files.write(imagePath, "fake content".getBytes());

            ResponseEntity<org.springframework.core.io.Resource> response = 
                imagenController.servirImagen(formats[i]);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(expectedTypes[i], response.getHeaders().getContentType().toString());
        }
    }

    @Test
    void testServirImagen_FormatoDesconocido() throws IOException {
        // Crear archivo con formato desconocido
        String filename = "test.xyz";
        Path imagePath = uploadsDir.resolve(filename);
        Files.write(imagePath, "fake content".getBytes());

        ResponseEntity<org.springframework.core.io.Resource> response = 
            imagenController.servirImagen(filename);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("application/octet-stream", response.getHeaders().getContentType().toString());
    }

    @Test
    void testImagenExiste_Exito() throws IOException {
        // Crear archivo de imagen
        String filename = "test.jpg";
        Path imagePath = uploadsDir.resolve(filename);
        Files.write(imagePath, "fake content".getBytes());

        ResponseEntity<Boolean> response = imagenController.imagenExiste(filename);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    void testImagenExiste_NoExiste() {
        ResponseEntity<Boolean> response = imagenController.imagenExiste("nonexistent.jpg");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }

    @Test
    void testImagenExiste_NombreArchivoInvalido() {
        ResponseEntity<Boolean> response = imagenController.imagenExiste("../malicious.jpg");

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }

    @Test
    void testImagenExiste_PathTraversal() {
        ResponseEntity<Boolean> response = imagenController.imagenExiste("../../../etc/passwd");

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }

    @Test
    void testImagenExiste_NombreConBarras() {
        ResponseEntity<Boolean> response = imagenController.imagenExiste("folder/image.jpg");

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }

    @Test
    void testImagenExiste_NombreConBackslashes() {
        ResponseEntity<Boolean> response = imagenController.imagenExiste("folder\\image.jpg");

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }
} 