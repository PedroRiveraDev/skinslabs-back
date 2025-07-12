package com.skinslabs.back.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuncionTest {

    @Test
    void testConstructorAndGetters() {
        String descripcion = "Función de prueba";
        BotServicio botServicio = new BotServicio();
        botServicio.setId(1L);
        
        Funcion funcion = new Funcion();
        funcion.setDescripcion(descripcion);
        funcion.setBotServicio(botServicio);
        
        assertEquals(descripcion, funcion.getDescripcion());
        assertEquals(botServicio, funcion.getBotServicio());
    }

    @Test
    void testSetters() {
        Funcion funcion = new Funcion();
        
        String nuevaDescripcion = "Nueva función";
        BotServicio nuevoBot = new BotServicio();
        nuevoBot.setId(2L);
        
        funcion.setDescripcion(nuevaDescripcion);
        funcion.setBotServicio(nuevoBot);
        
        assertEquals(nuevaDescripcion, funcion.getDescripcion());
        assertEquals(nuevoBot, funcion.getBotServicio());
    }

    @Test
    void testId() {
        Funcion funcion = new Funcion();
        Long id = 1L;
        
        funcion.setId(id);
        
        assertEquals(id, funcion.getId());
    }

    @Test
    void testNullValues() {
        Funcion funcion = new Funcion();
        
        funcion.setDescripcion(null);
        funcion.setBotServicio(null);
        
        assertNull(funcion.getDescripcion());
        assertNull(funcion.getBotServicio());
    }
} 