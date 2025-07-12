package com.skinslabs.back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TecnologiaTest {

    @Test
    void testConstructorAndGetters() {
        String nombre = "Tecnología de prueba";
        BotServicio botServicio = new BotServicio();
        botServicio.setId(1L);
        
        Tecnologia tecnologia = new Tecnologia();
        tecnologia.setNombre(nombre);
        tecnologia.setBotServicio(botServicio);
        
        assertEquals(nombre, tecnologia.getNombre());
        assertEquals(botServicio, tecnologia.getBotServicio());
    }

    @Test
    void testSetters() {
        Tecnologia tecnologia = new Tecnologia();
        
        String nuevoNombre = "Nueva tecnología";
        BotServicio nuevoBot = new BotServicio();
        nuevoBot.setId(2L);
        
        tecnologia.setNombre(nuevoNombre);
        tecnologia.setBotServicio(nuevoBot);
        
        assertEquals(nuevoNombre, tecnologia.getNombre());
        assertEquals(nuevoBot, tecnologia.getBotServicio());
    }

    @Test
    void testId() {
        Tecnologia tecnologia = new Tecnologia();
        Long id = 1L;
        
        tecnologia.setId(id);
        
        assertEquals(id, tecnologia.getId());
    }

    @Test
    void testNullValues() {
        Tecnologia tecnologia = new Tecnologia();
        
        tecnologia.setNombre(null);
        tecnologia.setBotServicio(null);
        
        assertNull(tecnologia.getNombre());
        assertNull(tecnologia.getBotServicio());
    }
} 