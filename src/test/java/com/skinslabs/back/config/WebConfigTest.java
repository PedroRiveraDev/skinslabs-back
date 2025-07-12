package com.skinslabs.back.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class WebConfigTest {

    @Autowired
    private WebConfig webConfig;

    @Test
    void testWebConfigExists() {
        // Verificar que la clase WebConfig existe y se puede autowirear
        assertNotNull(webConfig);
    }

    @Test
    void testWebConfigImplementsWebMvcConfigurer() {
        // Verificar que WebConfig implementa WebMvcConfigurer
        assertTrue(webConfig instanceof WebMvcConfigurer);
    }
} 