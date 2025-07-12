package com.skinslabs.back;

import com.skinslabs.back.model.*;
import com.skinslabs.back.service.BotServicioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BackApplicationIntegrationTest {

    @Autowired
    private BotServicioService botServicioService;

    @Test
    void testContextLoads() {
        // Este test verifica que el contexto de Spring se carga correctamente
        assertNotNull(botServicioService);
    }

    @Test
    void testCrearYRecuperarBot() {
        // Arrange
        BotServicio bot = new BotServicio();
        bot.setTitulo("Bot de Integración");
        bot.setDescripcion("Bot para test de integración");
        bot.setImagenUrl("http://example.com/integration.jpg");

        // Act
        BotServicio botCreado = botServicioService.crear(bot);
        Optional<BotServicio> botRecuperado = botServicioService.obtenerPorId(botCreado.getId());

        // Assert
        assertTrue(botRecuperado.isPresent());
        assertEquals("Bot de Integración", botRecuperado.get().getTitulo());
        assertEquals("Bot para test de integración", botRecuperado.get().getDescripcion());
        assertEquals("http://example.com/integration.jpg", botRecuperado.get().getImagenUrl());
    }

    @Test
    void testCrearBotConRelaciones() {
        // Arrange
        BotServicio bot = new BotServicio();
        bot.setTitulo("Bot con Relaciones");
        bot.setDescripcion("Bot que tiene todas las relaciones");

        // Crear funciones
        Funcion funcion1 = new Funcion();
        funcion1.setDescripcion("Función 1");
        Funcion funcion2 = new Funcion();
        funcion2.setDescripcion("Función 2");
        bot.setFunciones(Arrays.asList(funcion1, funcion2));

        // Crear integraciones
        Integracion integracion1 = new Integracion();
        integracion1.setNombre("Integración 1");
        Integracion integracion2 = new Integracion();
        integracion2.setNombre("Integración 2");
        bot.setIntegraciones(Arrays.asList(integracion1, integracion2));

        // Crear casos de uso
        CasoUso casoUso1 = new CasoUso();
        casoUso1.setDescripcion("Caso de uso 1");
        CasoUso casoUso2 = new CasoUso();
        casoUso2.setDescripcion("Caso de uso 2");
        bot.setCasosUso(Arrays.asList(casoUso1, casoUso2));

        // Crear tecnologías
        Tecnologia tecnologia1 = new Tecnologia();
        tecnologia1.setNombre("Java");
        Tecnologia tecnologia2 = new Tecnologia();
        tecnologia2.setNombre("Spring Boot");
        bot.setTecnologias(Arrays.asList(tecnologia1, tecnologia2));

        // Crear flujos automatizados
        FlujoAutomatizado flujo1 = new FlujoAutomatizado();
        flujo1.setDescripcion("Flujo 1");
        FlujoAutomatizado flujo2 = new FlujoAutomatizado();
        flujo2.setDescripcion("Flujo 2");
        bot.setFlujosAutomatizados(Arrays.asList(flujo1, flujo2));

        // Crear requisitos
        Requisito requisito1 = new Requisito();
        requisito1.setDescripcion("Requisito 1");
        Requisito requisito2 = new Requisito();
        requisito2.setDescripcion("Requisito 2");
        bot.setRequisitos(Arrays.asList(requisito1, requisito2));

        // Act
        BotServicio botCreado = botServicioService.crear(bot);

        // Assert
        assertNotNull(botCreado);
        assertNotNull(botCreado.getId());
        assertEquals("Bot con Relaciones", botCreado.getTitulo());
        
        // Verificar que todas las relaciones se crearon correctamente
        assertEquals(2, botCreado.getFunciones().size());
        assertEquals(2, botCreado.getIntegraciones().size());
        assertEquals(2, botCreado.getCasosUso().size());
        assertEquals(2, botCreado.getTecnologias().size());
        assertEquals(2, botCreado.getFlujosAutomatizados().size());
        assertEquals(2, botCreado.getRequisitos().size());

        // Verificar que las relaciones tienen la referencia correcta al bot padre
        botCreado.getFunciones().forEach(f -> assertEquals(botCreado, f.getBotServicio()));
        botCreado.getIntegraciones().forEach(i -> assertEquals(botCreado, i.getBotServicio()));
        botCreado.getCasosUso().forEach(c -> assertEquals(botCreado, c.getBotServicio()));
        botCreado.getTecnologias().forEach(t -> assertEquals(botCreado, t.getBotServicio()));
        botCreado.getFlujosAutomatizados().forEach(f -> assertEquals(botCreado, f.getBotServicio()));
        botCreado.getRequisitos().forEach(r -> assertEquals(botCreado, r.getBotServicio()));
    }

    @Test
    void testActualizarBot() {
        // Arrange
        BotServicio bot = new BotServicio();
        bot.setTitulo("Bot Original");
        bot.setDescripcion("Descripción original");
        bot.setImagenUrl("http://example.com/original.jpg");

        BotServicio botCreado = botServicioService.crear(bot);

        BotServicio botActualizado = new BotServicio();
        botActualizado.setTitulo("Bot Actualizado");
        botActualizado.setDescripcion("Descripción actualizada");
        botActualizado.setImagenUrl("http://example.com/updated.jpg");

        // Act
        BotServicio resultado = botServicioService.actualizar(botCreado.getId(), botActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Bot Actualizado", resultado.getTitulo());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        assertEquals("http://example.com/updated.jpg", resultado.getImagenUrl());
    }

    @Test
    void testObtenerTodosLosBots() {
        // Arrange
        BotServicio bot1 = new BotServicio();
        bot1.setTitulo("Bot 1");
        bot1.setDescripcion("Descripción del bot 1");

        BotServicio bot2 = new BotServicio();
        bot2.setTitulo("Bot 2");
        bot2.setDescripcion("Descripción del bot 2");

        botServicioService.crear(bot1);
        botServicioService.crear(bot2);

        // Act
        List<BotServicio> todosLosBots = botServicioService.obtenerTodos();

        // Assert
        assertNotNull(todosLosBots);
        assertTrue(todosLosBots.size() >= 2);
        
        // Verificar que ambos bots están en la lista
        boolean foundBot1 = todosLosBots.stream()
                .anyMatch(bot -> "Bot 1".equals(bot.getTitulo()));
        boolean foundBot2 = todosLosBots.stream()
                .anyMatch(bot -> "Bot 2".equals(bot.getTitulo()));
        
        assertTrue(foundBot1);
        assertTrue(foundBot2);
    }

    @Test
    void testEliminarBot() {
        // Arrange
        BotServicio bot = new BotServicio();
        bot.setTitulo("Bot para Eliminar");
        bot.setDescripcion("Bot que será eliminado");

        BotServicio botCreado = botServicioService.crear(bot);
        Long botId = botCreado.getId();

        // Verificar que el bot existe
        Optional<BotServicio> botAntes = botServicioService.obtenerPorId(botId);
        assertTrue(botAntes.isPresent());

        // Act
        botServicioService.eliminar(botId);

        // Assert
        Optional<BotServicio> botDespues = botServicioService.obtenerPorId(botId);
        assertFalse(botDespues.isPresent());
    }

    @Test
    void testActualizarBotConRelaciones() {
        // Arrange
        BotServicio bot = new BotServicio();
        bot.setTitulo("Bot Original");
        bot.setDescripcion("Descripción original");

        // Agregar una función inicial
        Funcion funcionOriginal = new Funcion();
        funcionOriginal.setDescripcion("Función original");
        bot.setFunciones(Arrays.asList(funcionOriginal));

        BotServicio botCreado = botServicioService.crear(bot);

        // Crear bot actualizado con nuevas relaciones
        BotServicio botActualizado = new BotServicio();
        botActualizado.setTitulo("Bot Actualizado");
        botActualizado.setDescripcion("Descripción actualizada");

        // Agregar nuevas funciones
        Funcion nuevaFuncion1 = new Funcion();
        nuevaFuncion1.setDescripcion("Nueva función 1");
        Funcion nuevaFuncion2 = new Funcion();
        nuevaFuncion2.setDescripcion("Nueva función 2");
        botActualizado.setFunciones(Arrays.asList(nuevaFuncion1, nuevaFuncion2));

        // Agregar nuevas integraciones
        Integracion nuevaIntegracion = new Integracion();
        nuevaIntegracion.setNombre("Nueva integración");
        botActualizado.setIntegraciones(Arrays.asList(nuevaIntegracion));

        // Act
        BotServicio resultado = botServicioService.actualizar(botCreado.getId(), botActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Bot Actualizado", resultado.getTitulo());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        
        // Verificar que las relaciones fueron actualizadas
        assertEquals(2, resultado.getFunciones().size());
        assertEquals(1, resultado.getIntegraciones().size());
        
        // Verificar que las nuevas relaciones tienen la referencia correcta al bot padre
        resultado.getFunciones().forEach(f -> assertEquals(resultado, f.getBotServicio()));
        resultado.getIntegraciones().forEach(i -> assertEquals(resultado, i.getBotServicio()));
    }
} 