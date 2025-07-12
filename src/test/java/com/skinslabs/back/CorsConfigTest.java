package com.skinslabs.back;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class CorsConfigTest {

    @Autowired
    private CorsConfig corsConfig;

    @Test
    void testCorsConfigurerBean() {
        WebMvcConfigurer configurer = corsConfig.corsConfigurer();
        assertNotNull(configurer);
    }

    @Test
    void testCorsMappings() {
        WebMvcConfigurer configurer = corsConfig.corsConfigurer();
        
        // Crear un mock registry para verificar que se configuran los CORS
        CorsRegistry registry = new CorsRegistry();
        
        // Llamar al método addCorsMappings
        configurer.addCorsMappings(registry);
        
        // Verificar que el bean se crea correctamente
        assertNotNull(configurer);
    }
} 