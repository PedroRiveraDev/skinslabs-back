package com.skinslabs.back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CasoUsoTest {

    @Test
    void testConstructorAndGetters() {
        String descripcion = "Caso de uso de prueba";
        BotServicio botServicio = new BotServicio();
        botServicio.setId(1L);
        
        CasoUso casoUso = new CasoUso();
        casoUso.setDescripcion(descripcion);
        casoUso.setBotServicio(botServicio);
        
        assertEquals(descripcion, casoUso.getDescripcion());
        assertEquals(botServicio, casoUso.getBotServicio());
    }

    @Test
    void testSetters() {
        CasoUso casoUso = new CasoUso();
        
        String nuevaDescripcion = "Nueva descripción";
        BotServicio nuevoBot = new BotServicio();
        nuevoBot.setId(2L);
        
        casoUso.setDescripcion(nuevaDescripcion);
        casoUso.setBotServicio(nuevoBot);
        
        assertEquals(nuevaDescripcion, casoUso.getDescripcion());
        assertEquals(nuevoBot, casoUso.getBotServicio());
    }

    @Test
    void testId() {
        CasoUso casoUso = new CasoUso();
        Long id = 1L;
        
        casoUso.setId(id);
        
        assertEquals(id, casoUso.getId());
    }

    @Test
    void testNullValues() {
        CasoUso casoUso = new CasoUso();
        
        casoUso.setDescripcion(null);
        casoUso.setBotServicio(null);
        
        assertNull(casoUso.getDescripcion());
        assertNull(casoUso.getBotServicio());
    }
} 