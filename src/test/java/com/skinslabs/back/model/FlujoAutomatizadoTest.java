package com.skinslabs.back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlujoAutomatizadoTest {

    @Test
    void testConstructorAndGetters() {
        String descripcion = "Flujo automatizado de prueba";
        BotServicio botServicio = new BotServicio();
        botServicio.setId(1L);
        
        FlujoAutomatizado flujo = new FlujoAutomatizado();
        flujo.setDescripcion(descripcion);
        flujo.setBotServicio(botServicio);
        
        assertEquals(descripcion, flujo.getDescripcion());
        assertEquals(botServicio, flujo.getBotServicio());
    }

    @Test
    void testSetters() {
        FlujoAutomatizado flujo = new FlujoAutomatizado();
        
        String nuevaDescripcion = "Nuevo flujo automatizado";
        BotServicio nuevoBot = new BotServicio();
        nuevoBot.setId(2L);
        
        flujo.setDescripcion(nuevaDescripcion);
        flujo.setBotServicio(nuevoBot);
        
        assertEquals(nuevaDescripcion, flujo.getDescripcion());
        assertEquals(nuevoBot, flujo.getBotServicio());
    }

    @Test
    void testId() {
        FlujoAutomatizado flujo = new FlujoAutomatizado();
        Long id = 1L;
        
        flujo.setId(id);
        
        assertEquals(id, flujo.getId());
    }

    @Test
    void testNullValues() {
        FlujoAutomatizado flujo = new FlujoAutomatizado();
        
        flujo.setDescripcion(null);
        flujo.setBotServicio(null);
        
        assertNull(flujo.getDescripcion());
        assertNull(flujo.getBotServicio());
    }
} 