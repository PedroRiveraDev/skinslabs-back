package com.skinslabs.back.controller;

import com.skinslabs.back.model.*;
import com.skinslabs.back.service.BotServicioService;
import com.skinslabs.back.service.RelacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BotServicioGraphQLControllerTest {

    @Mock
    private BotServicioService botServicioService;

    @Mock
    private RelacionService relacionService;

    @InjectMocks
    private BotServicioGraphQLController controller;

    private BotServicio botServicio;
    private Funcion funcion;
    private Integracion integracion;
    private CasoUso casoUso;
    private Tecnologia tecnologia;
    private FlujoAutomatizado flujoAutomatizado;
    private Requisito requisito;

    @BeforeEach
    void setUp() {
        botServicio = new BotServicio();
        botServicio.setId(1L);
        botServicio.setTitulo("Bot de Prueba");
        botServicio.setDescripcion("Descripción del bot de prueba");
        botServicio.setImagenUrl("http://example.com/image.jpg");

        funcion = new Funcion();
        funcion.setId(1L);
        funcion.setDescripcion("Función de prueba");

        integracion = new Integracion();
        integracion.setId(1L);
        integracion.setNombre("Integración de prueba");

        casoUso = new CasoUso();
        casoUso.setId(1L);
        casoUso.setDescripcion("Caso de uso de prueba");

        tecnologia = new Tecnologia();
        tecnologia.setId(1L);
        tecnologia.setNombre("Tecnología de prueba");

        flujoAutomatizado = new FlujoAutomatizado();
        flujoAutomatizado.setId(1L);
        flujoAutomatizado.setDescripcion("Flujo automatizado de prueba");

        requisito = new Requisito();
        requisito.setId(1L);
        requisito.setDescripcion("Requisito de prueba");
    }

    @Test
    void testObtenerBots() {
        // Arrange
        List<BotServicio> bots = Arrays.asList(botServicio);
        when(botServicioService.obtenerTodos()).thenReturn(bots);

        // Act
        List<BotServicio> resultado = controller.obtenerBots();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(botServicio, resultado.get(0));
        verify(botServicioService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerBotPorId_Existe() {
        // Arrange
        when(botServicioService.obtenerPorId(1L)).thenReturn(Optional.of(botServicio));

        // Act
        BotServicio resultado = controller.obtenerBotPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(botServicio, resultado);
        verify(botServicioService, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerBotPorId_NoExiste() {
        // Arrange
        when(botServicioService.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act
        BotServicio resultado = controller.obtenerBotPorId(999L);

        // Assert
        assertNull(resultado);
        verify(botServicioService, times(1)).obtenerPorId(999L);
    }

    @Test
    void testCrearBot() {
        // Arrange
        BotServicio botNuevo = new BotServicio();
        botNuevo.setTitulo("Nuevo Bot");
        botNuevo.setDescripcion("Descripción del nuevo bot");

        when(botServicioService.crear(any(BotServicio.class))).thenReturn(botNuevo);

        // Act
        BotServicio resultado = controller.crearBot(botNuevo);

        // Assert
        assertNotNull(resultado);
        assertEquals("Nuevo Bot", resultado.getTitulo());
        verify(botServicioService, times(1)).crear(botNuevo);
    }

    @Test
    void testActualizarBot() {
        // Arrange
        BotServicio botActualizado = new BotServicio();
        botActualizado.setTitulo("Bot Actualizado");
        botActualizado.setDescripcion("Descripción actualizada");

        when(botServicioService.actualizar(eq(1L), any(BotServicio.class))).thenReturn(botActualizado);

        // Act
        BotServicio resultado = controller.actualizarBot(1L, botActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Bot Actualizado", resultado.getTitulo());
        verify(botServicioService, times(1)).actualizar(1L, botActualizado);
    }

    @Test
    void testEliminarBot() {
        // Arrange
        doNothing().when(botServicioService).eliminar(1L);

        // Act
        Boolean resultado = controller.eliminarBot(1L);

        // Assert
        assertTrue(resultado);
        verify(botServicioService, times(1)).eliminar(1L);
    }

    @Test
    void testAgregarFuncion() {
        // Arrange
        when(relacionService.agregarFuncion(eq(1L), any(Funcion.class))).thenReturn(funcion);

        // Act
        Funcion resultado = controller.agregarFuncion(1L, funcion);

        // Assert
        assertNotNull(resultado);
        assertEquals(funcion, resultado);
        verify(relacionService, times(1)).agregarFuncion(1L, funcion);
    }

    @Test
    void testAgregarIntegracion() {
        // Arrange
        when(relacionService.agregarIntegracion(eq(1L), any(Integracion.class))).thenReturn(integracion);

        // Act
        Integracion resultado = controller.agregarIntegracion(1L, integracion);

        // Assert
        assertNotNull(resultado);
        assertEquals(integracion, resultado);
        verify(relacionService, times(1)).agregarIntegracion(1L, integracion);
    }

    @Test
    void testAgregarCasoUso() {
        // Arrange
        when(relacionService.agregarCasoUso(eq(1L), any(CasoUso.class))).thenReturn(casoUso);

        // Act
        CasoUso resultado = controller.agregarCasoUso(1L, casoUso);

        // Assert
        assertNotNull(resultado);
        assertEquals(casoUso, resultado);
        verify(relacionService, times(1)).agregarCasoUso(1L, casoUso);
    }

    @Test
    void testAgregarTecnologia() {
        // Arrange
        when(relacionService.agregarTecnologia(eq(1L), any(Tecnologia.class))).thenReturn(tecnologia);

        // Act
        Tecnologia resultado = controller.agregarTecnologia(1L, tecnologia);

        // Assert
        assertNotNull(resultado);
        assertEquals(tecnologia, resultado);
        verify(relacionService, times(1)).agregarTecnologia(1L, tecnologia);
    }

    @Test
    void testAgregarFlujoAutomatizado() {
        // Arrange
        when(relacionService.agregarFlujoAutomatizado(eq(1L), any(FlujoAutomatizado.class))).thenReturn(flujoAutomatizado);

        // Act
        FlujoAutomatizado resultado = controller.agregarFlujoAutomatizado(1L, flujoAutomatizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(flujoAutomatizado, resultado);
        verify(relacionService, times(1)).agregarFlujoAutomatizado(1L, flujoAutomatizado);
    }

    @Test
    void testAgregarRequisito() {
        // Arrange
        when(relacionService.agregarRequisito(eq(1L), any(Requisito.class))).thenReturn(requisito);

        // Act
        Requisito resultado = controller.agregarRequisito(1L, requisito);

        // Assert
        assertNotNull(resultado);
        assertEquals(requisito, resultado);
        verify(relacionService, times(1)).agregarRequisito(1L, requisito);
    }

    @Test
    void actualizarBot_ConOrphanRemoval_DebeReemplazarCasosUsoCorrectamente() {
        // Arrange - Crear bot actualizado con nuevos casos de uso
        BotServicio botActualizado = new BotServicio();
        botActualizado.setTitulo("Bot Actualizado");
        botActualizado.setDescripcion("Descripción actualizada");
        
        CasoUso nuevoCasoUso1 = new CasoUso();
        nuevoCasoUso1.setDescripcion("Nuevo caso de uso 1");
        
        CasoUso nuevoCasoUso2 = new CasoUso();
        nuevoCasoUso2.setDescripcion("Nuevo caso de uso 2");
        
        CasoUso nuevoCasoUso3 = new CasoUso();
        nuevoCasoUso3.setDescripcion("Nuevo caso de uso 3");
        
        botActualizado.setCasosUso(Arrays.asList(nuevoCasoUso1, nuevoCasoUso2, nuevoCasoUso3));

        // Configurar el mock para devolver el bot actualizado
        when(botServicioService.actualizar(eq(1L), any(BotServicio.class))).thenReturn(botActualizado);

        // Act
        BotServicio resultado = controller.actualizarBot(1L, botActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Bot Actualizado", resultado.getTitulo());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        
        // Verificar que los casos de uso fueron configurados correctamente
        List<CasoUso> casosUsoResultado = resultado.getCasosUso();
        assertEquals(3, casosUsoResultado.size());
        
        // Verificar que los nuevos casos de uso tienen la referencia correcta al bot
        casosUsoResultado.forEach(casoUso -> {
            assertEquals(resultado, casoUso.getBotServicio());
            assertTrue(casoUso.getDescripcion().startsWith("Nuevo caso de uso"));
        });
        
        // Verificar que el servicio fue llamado correctamente
        verify(botServicioService, times(1)).actualizar(1L, botActualizado);
    }

    @Test
    void actualizarBot_ConListaVacia_DebeLimpiarCasosUso() {
        // Arrange - Crear bot actualizado con lista vacía
        BotServicio botActualizado = new BotServicio();
        botActualizado.setTitulo("Bot Actualizado");
        botActualizado.setDescripcion("Descripción actualizada");
        botActualizado.setCasosUso(new ArrayList<>()); // Lista vacía

        // Configurar el mock para devolver el bot actualizado
        when(botServicioService.actualizar(eq(1L), any(BotServicio.class))).thenReturn(botActualizado);

        // Act
        BotServicio resultado = controller.actualizarBot(1L, botActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Bot Actualizado", resultado.getTitulo());
        assertTrue(resultado.getCasosUso().isEmpty());
        
        // Verificar que el servicio fue llamado correctamente
        verify(botServicioService, times(1)).actualizar(1L, botActualizado);
    }
} 