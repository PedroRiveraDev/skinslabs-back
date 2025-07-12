package com.skinslabs.back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequisitoTest {

    @Test
    void testConstructorAndGetters() {
        String descripcion = "Requisito de prueba";
        BotServicio botServicio = new BotServicio();
        botServicio.setId(1L);
        
        Requisito requisito = new Requisito();
        requisito.setDescripcion(descripcion);
        requisito.setBotServicio(botServicio);
        
        assertEquals(descripcion, requisito.getDescripcion());
        assertEquals(botServicio, requisito.getBotServicio());
    }

    @Test
    void testSetters() {
        Requisito requisito = new Requisito();
        
        String nuevaDescripcion = "Nuevo requisito";
        BotServicio nuevoBot = new BotServicio();
        nuevoBot.setId(2L);
        
        requisito.setDescripcion(nuevaDescripcion);
        requisito.setBotServicio(nuevoBot);
        
        assertEquals(nuevaDescripcion, requisito.getDescripcion());
        assertEquals(nuevoBot, requisito.getBotServicio());
    }

    @Test
    void testId() {
        Requisito requisito = new Requisito();
        Long id = 1L;
        
        requisito.setId(id);
        
        assertEquals(id, requisito.getId());
    }

    @Test
    void testNullValues() {
        Requisito requisito = new Requisito();
        
        requisito.setDescripcion(null);
        requisito.setBotServicio(null);
        
        assertNull(requisito.getDescripcion());
        assertNull(requisito.getBotServicio());
    }
} 