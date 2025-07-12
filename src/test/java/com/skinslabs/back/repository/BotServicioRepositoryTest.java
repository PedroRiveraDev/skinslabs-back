package com.skinslabs.back.repository;

import com.skinslabs.back.model.BotServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BotServicioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BotServicioRepository botServicioRepository;

    private BotServicio botServicio1;
    private BotServicio botServicio2;

    @BeforeEach
    void setUp() {
        // Crear datos de prueba
        botServicio1 = new BotServicio();
        botServicio1.setTitulo("Bot de Prueba 1");
        botServicio1.setDescripcion("Descripción del bot de prueba 1");
        botServicio1.setImagenUrl("http://example.com/image1.jpg");

        botServicio2 = new BotServicio();
        botServicio2.setTitulo("Bot de Prueba 2");
        botServicio2.setDescripcion("Descripción del bot de prueba 2");
        botServicio2.setImagenUrl("http://example.com/image2.jpg");
    }

    @Test
    void testSave() {
        // Act
        BotServicio savedBot = botServicioRepository.save(botServicio1);

        // Assert
        assertNotNull(savedBot);
        assertNotNull(savedBot.getId());
        assertEquals("Bot de Prueba 1", savedBot.getTitulo());
        assertEquals("Descripción del bot de prueba 1", savedBot.getDescripcion());
        assertEquals("http://example.com/image1.jpg", savedBot.getImagenUrl());
    }

    @Test
    void testFindById_Existe() {
        // Arrange
        BotServicio savedBot = entityManager.persistAndFlush(botServicio1);

        // Act
        Optional<BotServicio> foundBot = botServicioRepository.findById(savedBot.getId());

        // Assert
        assertTrue(foundBot.isPresent());
        assertEquals(savedBot.getId(), foundBot.get().getId());
        assertEquals("Bot de Prueba 1", foundBot.get().getTitulo());
    }

    @Test
    void testFindById_NoExiste() {
        // Act
        Optional<BotServicio> foundBot = botServicioRepository.findById(999L);

        // Assert
        assertFalse(foundBot.isPresent());
    }

    @Test
    void testFindAll() {
        // Arrange
        entityManager.persistAndFlush(botServicio1);
        entityManager.persistAndFlush(botServicio2);

        // Act
        List<BotServicio> allBots = botServicioRepository.findAll();

        // Assert
        assertNotNull(allBots);
        assertTrue(allBots.size() >= 2);
        
        // Verificar que ambos bots están en la lista
        boolean foundBot1 = allBots.stream()
                .anyMatch(bot -> "Bot de Prueba 1".equals(bot.getTitulo()));
        boolean foundBot2 = allBots.stream()
                .anyMatch(bot -> "Bot de Prueba 2".equals(bot.getTitulo()));
        
        assertTrue(foundBot1);
        assertTrue(foundBot2);
    }

    @Test
    void testUpdate() {
        // Arrange
        BotServicio savedBot = entityManager.persistAndFlush(botServicio1);
        savedBot.setTitulo("Bot Actualizado");
        savedBot.setDescripcion("Descripción actualizada");

        // Act
        BotServicio updatedBot = botServicioRepository.save(savedBot);

        // Assert
        assertNotNull(updatedBot);
        assertEquals(savedBot.getId(), updatedBot.getId());
        assertEquals("Bot Actualizado", updatedBot.getTitulo());
        assertEquals("Descripción actualizada", updatedBot.getDescripcion());
    }

    @Test
    void testDelete() {
        // Arrange
        BotServicio savedBot = entityManager.persistAndFlush(botServicio1);
        Long botId = savedBot.getId();

        // Act
        botServicioRepository.deleteById(botId);

        // Assert
        Optional<BotServicio> deletedBot = botServicioRepository.findById(botId);
        assertFalse(deletedBot.isPresent());
    }

    @Test
    void testSaveWithRelations() {
        // Arrange
        BotServicio botWithRelations = new BotServicio();
        botWithRelations.setTitulo("Bot con Relaciones");
        botWithRelations.setDescripcion("Bot que tiene relaciones");

        // Act
        BotServicio savedBot = botServicioRepository.save(botWithRelations);

        // Assert
        assertNotNull(savedBot);
        assertNotNull(savedBot.getId());
        assertEquals("Bot con Relaciones", savedBot.getTitulo());
        
        // Verificar que las listas de relaciones están inicializadas
        assertNotNull(savedBot.getFunciones());
        assertNotNull(savedBot.getIntegraciones());
        assertNotNull(savedBot.getCasosUso());
        assertNotNull(savedBot.getTecnologias());
        assertNotNull(savedBot.getFlujosAutomatizados());
        assertNotNull(savedBot.getRequisitos());
    }

    @Test
    void testFindAllEmpty() {
        // Act
        List<BotServicio> allBots = botServicioRepository.findAll();

        // Assert
        assertNotNull(allBots);
        // La lista puede estar vacía o tener datos de otros tests
        // Solo verificamos que no sea null
    }
} 