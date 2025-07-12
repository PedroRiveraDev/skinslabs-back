package com.skinslabs.back.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotsCatalogoTest {

    @Test
    void testConstructorAndGetters() {
        String titulo = "Bot de Prueba";
        String descripcion = "Descripción de prueba";
        
        BotsCatalogo bot = new BotsCatalogo(titulo, descripcion);
        
        assertEquals(titulo, bot.getTitulo());
        assertEquals(descripcion, bot.getDescripcion());
    }

    @Test
    void testSetters() {
        BotsCatalogo bot = new BotsCatalogo("Título original", "Descripción original");
        
        String nuevoTitulo = "Nuevo título";
        String nuevaDescripcion = "Nueva descripción";
        
        bot.setTitulo(nuevoTitulo);
        bot.setDescripcion(nuevaDescripcion);
        
        assertEquals(nuevoTitulo, bot.getTitulo());
        assertEquals(nuevaDescripcion, bot.getDescripcion());
    }

    @Test
    void testConstructorWithNullValues() {
        BotsCatalogo bot = new BotsCatalogo(null, null);
        
        assertNull(bot.getTitulo());
        assertNull(bot.getDescripcion());
    }

    @Test
    void testSettersWithNullValues() {
        BotsCatalogo bot = new BotsCatalogo("Título", "Descripción");
        
        bot.setTitulo(null);
        bot.setDescripcion(null);
        
        assertNull(bot.getTitulo());
        assertNull(bot.getDescripcion());
    }
} 