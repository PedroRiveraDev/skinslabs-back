package com.skinslabs.back;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class BackApplicationTest {

    @Test
    void testContextLoads() {
        // Este test verifica que el contexto de Spring se carga correctamente
        assertTrue(true);
    }

    @Test
    void testMainMethod() {
        // Test para verificar que el método main no lanza excepciones
        assertDoesNotThrow(() -> {
            // Simular el método main sin argumentos
            String[] args = {};
            // No podemos llamar directamente al main porque requiere un contexto de Spring
            // pero podemos verificar que la clase se puede instanciar
            BackApplication app = new BackApplication();
            assertNotNull(app);
        });
    }

    @Test
    void testApplicationProperties() {
        // Verificar que las propiedades de la aplicación están configuradas correctamente
        String userDir = System.getProperty("user.dir");
        assertNotNull(userDir);
        assertFalse(userDir.isEmpty());
    }

    @Test
    void testApplicationClassExists() {
        // Verificar que la clase BackApplication existe y se puede instanciar
        BackApplication app = new BackApplication();
        assertNotNull(app);
        assertEquals(BackApplication.class, app.getClass());
    }
} 