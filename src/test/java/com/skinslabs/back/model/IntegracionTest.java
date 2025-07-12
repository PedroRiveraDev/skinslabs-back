package com.skinslabs.back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegracionTest {

    @Test
    void testConstructorAndGetters() {
        String nombre = "Integración de prueba";
        BotServicio botServicio = new BotServicio();
        botServicio.setId(1L);
        
        Integracion integracion = new Integracion();
        integracion.setNombre(nombre);
        integracion.setBotServicio(botServicio);
        
        assertEquals(nombre, integracion.getNombre());
        assertEquals(botServicio, integracion.getBotServicio());
    }

    @Test
    void testSetters() {
        Integracion integracion = new Integracion();
        
        String nuevoNombre = "Nueva integración";
        BotServicio nuevoBot = new BotServicio();
        nuevoBot.setId(2L);
        
        integracion.setNombre(nuevoNombre);
        integracion.setBotServicio(nuevoBot);
        
        assertEquals(nuevoNombre, integracion.getNombre());
        assertEquals(nuevoBot, integracion.getBotServicio());
    }

    @Test
    void testId() {
        Integracion integracion = new Integracion();
        Long id = 1L;
        
        integracion.setId(id);
        
        assertEquals(id, integracion.getId());
    }

    @Test
    void testNullValues() {
        Integracion integracion = new Integracion();
        
        integracion.setNombre(null);
        integracion.setBotServicio(null);
        
        assertNull(integracion.getNombre());
        assertNull(integracion.getBotServicio());
    }
} 