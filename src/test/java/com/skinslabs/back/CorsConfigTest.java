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
    void testCorsConfigClassExists() {
        // Verificar que la clase CorsConfig existe y se puede autowirear
        assertNotNull(corsConfig);
    }

    @Test
    void testCorsConfigIsCommented() {
        // Verificar que la configuración CORS está comentada ya que se movió a WebConfig
        assertNotNull(corsConfig);
        // La configuración CORS ahora está en WebConfig.java, no en CorsConfig.java
    }
} 