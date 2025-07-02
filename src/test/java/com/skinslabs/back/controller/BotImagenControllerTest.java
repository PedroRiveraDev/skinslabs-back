package com.skinslabs.back.controller;

import com.skinslabs.back.model.BotServicio;
import com.skinslabs.back.repository.BotServicioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BotImagenControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BotServicioRepository botServicioRepository;

    private BotServicio bot;

    @BeforeEach
    void setUp() {
        botServicioRepository.deleteAll();
        bot = new BotServicio();
        bot.setTitulo("Bot Imagen");
        bot.setDescripcion("Bot para test de imagen");
        bot = botServicioRepository.save(bot);
    }

    @Test
    void testSubirImagen_exito() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "contenido-imagen".getBytes()
        );
        var result = mockMvc.perform(multipart("/api/bots/" + bot.getId() + "/imagen")
                .file(file))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertThat(response).contains("/uploads/bot_" + bot.getId() + "_");
        // Verifica que el campo imagenUrl se actualizó en la base
        BotServicio actualizado = botServicioRepository.findById(bot.getId()).orElseThrow();
        assertThat(actualizado.getImagenUrl()).contains("/uploads/bot_" + bot.getId() + "_");
    }

    @Test
    void testSubirImagen_botNoExiste() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "contenido-imagen".getBytes()
        );
        mockMvc.perform(multipart("/api/bots/9999/imagen").file(file))
                .andExpect(status().isNotFound());
    }
} 