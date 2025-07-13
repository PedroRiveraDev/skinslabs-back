package com.skinslabs.back.service;

import com.skinslabs.back.model.BotServicio;
import com.skinslabs.back.repository.BotServicioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ImagenServiceTest {

    @TempDir
    Path tempDir;

    private ImagenService imagenService;
    private BotServicioRepository botServicioRepository;
    private Path uploadsDir;

    @BeforeEach
    void setUp() throws IOException {
        // Crear directorio uploads temporal
        uploadsDir = tempDir.resolve("uploads");
        Files.createDirectories(uploadsDir);
        
        // Cambiar el directorio de trabajo temporalmente
        System.setProperty("user.dir", tempDir.toString());
        
        botServicioRepository = mock(BotServicioRepository.class);
        imagenService = new ImagenService(botServicioRepository);
    }

    @Test
    void testSubirImagenParaBot_Exito() throws IOException {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        bot.setTitulo("Test Bot");
        bot.setDescripcion("Test Description");

        MockMultipartFile file = new MockMultipartFile(
            "image", 
            "test.jpg", 
            "image/jpeg", 
            "fake image content".getBytes()
        );

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));
        when(botServicioRepository.save(any(BotServicio.class))).thenReturn(bot);

        // Act
        String result = imagenService.subirImagenParaBot(botId, file);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith("/uploads/"));
        assertTrue(result.contains("bot_" + botId + "_"));
        assertTrue(result.endsWith(".jpg"));

        // Verificar que el archivo se guardó
        File savedFile = new File(tempDir.resolve("uploads").toString() + File.separator + result.substring(9));
        assertTrue(savedFile.exists());

        verify(botServicioRepository).findById(botId);
        verify(botServicioRepository).save(any(BotServicio.class));
    }

    @Test
    void testSubirImagenParaBot_BotNoEncontrado() {
        // Arrange
        Long botId = 1L;
        MockMultipartFile file = new MockMultipartFile(
            "image", 
            "test.jpg", 
            "image/jpeg", 
            "fake image content".getBytes()
        );

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            imagenService.subirImagenParaBot(botId, file);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Bot no encontrado", exception.getReason());
    }

    @Test
    void testSubirImagenParaBot_ArchivoVacio() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]);

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            imagenService.subirImagenParaBot(botId, file);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No se proporcionó ningún archivo", exception.getReason());
    }

    @Test
    void testSubirImagenParaBot_TipoArchivoNoPermitido() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        
        MockMultipartFile file = new MockMultipartFile(
            "image", 
            "test.txt", 
            "text/plain", 
            "fake content".getBytes()
        );

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            imagenService.subirImagenParaBot(botId, file);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Tipo de archivo no permitido"));
    }

    @Test
    void testSubirImagenParaBot_ArchivoDemasiadoGrande() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        
        byte[] largeContent = new byte[11 * 1024 * 1024]; // 11MB
        MockMultipartFile file = new MockMultipartFile(
            "image", 
            "test.jpg", 
            "image/jpeg", 
            largeContent
        );

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            imagenService.subirImagenParaBot(botId, file);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("demasiado grande"));
    }

    @Test
    void testEliminarImagenDeBot_Exito() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        bot.setImagenUrl("/uploads/test.jpg");

        // Crear archivo de imagen
        File imageFile = new File(uploadsDir.resolve("test.jpg").toString());
        try {
            Files.write(imageFile.toPath(), "fake content".getBytes());
        } catch (IOException e) {
            fail("No se pudo crear el archivo de prueba");
        }

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));
        when(botServicioRepository.save(any(BotServicio.class))).thenReturn(bot);

        // Act
        assertDoesNotThrow(() -> imagenService.eliminarImagenDeBot(botId));

        // Assert
        assertFalse(imageFile.exists());
        verify(botServicioRepository).findById(botId);
        verify(botServicioRepository).save(any(BotServicio.class));
    }

    @Test
    void testEliminarImagenDeBot_BotNoEncontrado() {
        // Arrange
        Long botId = 1L;
        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            imagenService.eliminarImagenDeBot(botId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Bot no encontrado", exception.getReason());
    }

    @Test
    void testEliminarImagenDeBot_SinImagen() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        bot.setImagenUrl(null);

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            imagenService.eliminarImagenDeBot(botId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("El bot no tiene imagen asociada", exception.getReason());
    }

    @Test
    void testEliminarImagenDeBot_ImagenVacia() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        bot.setImagenUrl("");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            imagenService.eliminarImagenDeBot(botId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("El bot no tiene imagen asociada", exception.getReason());
    }

    @Test
    void testImagenExiste_Exito() throws IOException {
        // Arrange
        String filename = "test.jpg";
        Path imagePath = uploadsDir.resolve(filename);
        Files.write(imagePath, "fake content".getBytes());

        // Act
        boolean result = imagenService.imagenExiste(filename);

        // Assert
        assertTrue(result);
    }

    @Test
    void testImagenExiste_NoExiste() {
        // Act
        boolean result = imagenService.imagenExiste("nonexistent.jpg");

        // Assert
        assertFalse(result);
    }

    @Test
    void testImagenExiste_NombreNulo() {
        // Act
        boolean result = imagenService.imagenExiste(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void testImagenExiste_NombreVacio() {
        // Act
        boolean result = imagenService.imagenExiste("");

        // Assert
        assertFalse(result);
    }

    @Test
    void testImagenExiste_PathTraversal() {
        // Act
        boolean result = imagenService.imagenExiste("../../../etc/passwd");

        // Assert
        assertFalse(result);
    }

    @Test
    void testImagenExiste_NombreConBarras() {
        // Act
        boolean result = imagenService.imagenExiste("folder/image.jpg");

        // Assert
        assertFalse(result);
    }

    @Test
    void testImagenExiste_NombreConBackslashes() {
        // Act
        boolean result = imagenService.imagenExiste("folder\\image.jpg");

        // Assert
        assertFalse(result);
    }

    @Test
    void testSubirImagenParaBot_DiferentesFormatos() throws IOException {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);

        String[] formats = {"test.png", "test.gif", "test.webp"};
        String[] contentTypes = {"image/png", "image/gif", "image/webp"};

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));
        when(botServicioRepository.save(any(BotServicio.class))).thenReturn(bot);

        for (int i = 0; i < formats.length; i++) {
            MockMultipartFile file = new MockMultipartFile(
                "image", 
                formats[i], 
                contentTypes[i], 
                "fake content".getBytes()
            );

            // Act
            String result = imagenService.subirImagenParaBot(botId, file);

            // Assert
            assertNotNull(result);
            assertTrue(result.startsWith("/uploads/"));
            assertTrue(result.contains("bot_" + botId + "_"));
            assertTrue(result.endsWith(formats[i].substring(formats[i].lastIndexOf("."))));
        }
    }
} 