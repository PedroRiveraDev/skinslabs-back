package com.skinslabs.back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinslabs.back.model.BotServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BotServicioGraphQLControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String endpoint = "/graphql";

    @BeforeEach
    void setUp() {
        // Limpiar o preparar datos si es necesario
    }

    @Test
    void testCrearYObtenerBot() throws Exception {
        // Mutación para crear un bot
        String mutation = """
            mutation {
              crearBot(input: {
                titulo: \"Bot Test\"
                descripcion: \"Bot para pruebas\"
                imagenUrl: \"/uploads/test.jpg\"
                funciones: []
                integraciones: []
                casosUso: []
                tecnologias: []
                flujosAutomatizados: []
                requisitos: []
              }) {
                id
                titulo
                descripcion
                imagenUrl
              }
            }
        """;
        MvcResult result = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new GraphQLRequest(mutation))))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertThat(response).contains("Bot Test");
        assertThat(response).contains("/uploads/test.jpg");

        // Query para obtener todos los bots
        String query = """
            { obtenerBots { id titulo descripcion imagenUrl } }
        """;
        MvcResult result2 = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new GraphQLRequest(query))))
                .andExpect(status().isOk())
                .andReturn();
        String response2 = result2.getResponse().getContentAsString();
        assertThat(response2).contains("Bot Test");
    }

    @Test
    void testEliminarBot() throws Exception {
        // Crear bot primero
        String mutation = """
            mutation {
              crearBot(input: {
                titulo: \"Bot Eliminar\"
                descripcion: \"Bot para eliminar\"
                imagenUrl: \"/uploads/delete.jpg\"
                funciones: []
                integraciones: []
                casosUso: []
                tecnologias: []
                flujosAutomatizados: []
                requisitos: []
              }) {
                id
              }
            }
        """;
        MvcResult result = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new GraphQLRequest(mutation))))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        String id = response.replaceAll(".*\\\"id\\\":\\\"(\\d+)\\\".*", "$1");

        // Mutación para eliminar el bot
        String deleteMutation = String.format("mutation { eliminarBot(id: %s) }", id);
        MvcResult result2 = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new GraphQLRequest(deleteMutation))))
                .andExpect(status().isOk())
                .andReturn();
        String response2 = result2.getResponse().getContentAsString();
        assertThat(response2).contains("true");
    }

    // Clase auxiliar para requests GraphQL
    static class GraphQLRequest {
        public String query;
        public GraphQLRequest(String query) { this.query = query; }
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
    }
} 