package com.skinslabs.back.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BotsCatalogoController.class)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class BotsCatalogoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetCatalogo() throws Exception {
        mockMvc.perform(get("/catalogo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titulo").value("Bot de Reportes Automatizados"))
                .andExpect(jsonPath("$[0].descripcion").value("Genera y envía reportes con métricas clave semanal o mensualmente a través de Gmail o SendGrid."))
                .andExpect(jsonPath("$[1].titulo").value("Bot de Resumen de Conversaciones"))
                .andExpect(jsonPath("$[1].descripcion").value("Analiza conversaciones extensas y genera resúmenes automáticos utilizando GPT, ideal para auditoría."))
                .andExpect(jsonPath("$[2].titulo").value(" Bot Integrador de CRM"))
                .andExpect(jsonPath("$[2].descripcion").value("Conecta automáticamente con HubSpot, Notion o Trello para registrar leads, tareas o contactos de clientes."))
                .andExpect(jsonPath("$[3].titulo").value("Bot de Atención 24/7"))
                .andExpect(jsonPath("$[3].descripcion").value("Chatbot basado en GPT-4 que responde preguntas frecuentes, deriva solicitudes y automatiza respuestas por correo o WhatsApp."));
    }

    @Test
    void testCatalogoSize() throws Exception {
        mockMvc.perform(get("/catalogo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4));
    }
} 