package com.skinslabs.back.service;

import com.skinslabs.back.model.*;
import com.skinslabs.back.repository.BotServicioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BotServicioServiceTest {

    @Mock
    private BotServicioRepository botServicioRepository;

    @InjectMocks
    private BotServicioService botServicioService;

    private BotServicio botServicio;
    private BotServicio botServicioConRelaciones;

    @BeforeEach
    void setUp() {
        botServicio = new BotServicio();
        botServicio.setId(1L);
        botServicio.setTitulo("Bot de Prueba");
        botServicio.setDescripcion("Descripción del bot de prueba");
        botServicio.setImagenUrl("http://example.com/image.jpg");

        botServicioConRelaciones = new BotServicio();
        botServicioConRelaciones.setId(2L);
        botServicioConRelaciones.setTitulo("Bot con Relaciones");
        botServicioConRelaciones.setDescripcion("Bot que tiene relaciones");
        botServicioConRelaciones.setImagenUrl("http://example.com/image2.jpg");
    }

    @Test
    void testObtenerTodos() {
        // Arrange
        List<BotServicio> bots = Arrays.asList(botServicio, botServicioConRelaciones);
        when(botServicioRepository.findAll()).thenReturn(bots);

        // Act
        List<BotServicio> resultado = botServicioService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(botServicioRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_Existe() {
        // Arrange
        when(botServicioRepository.findById(1L)).thenReturn(Optional.of(botServicio));

        // Act
        Optional<BotServicio> resultado = botServicioService.obtenerPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(botServicio, resultado.get());
        verify(botServicioRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_NoExiste() {
        // Arrange
        when(botServicioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<BotServicio> resultado = botServicioService.obtenerPorId(999L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(botServicioRepository, times(1)).findById(999L);
    }

    @Test
    void testCrear_SinRelaciones() {
        // Arrange
        BotServicio botNuevo = new BotServicio();
        botNuevo.setTitulo("Nuevo Bot");
        botNuevo.setDescripcion("Descripción del nuevo bot");
        botNuevo.setImagenUrl("http://example.com/new.jpg");

        when(botServicioRepository.save(any(BotServicio.class))).thenReturn(botNuevo);

        // Act
        BotServicio resultado = botServicioService.crear(botNuevo);

        // Assert
        assertNotNull(resultado);
        assertEquals("Nuevo Bot", resultado.getTitulo());
        verify(botServicioRepository, times(1)).save(botNuevo);
    }

    @Test
    void testCrear_ConRelaciones() {
        // Arrange
        BotServicio botConRelaciones = new BotServicio();
        botConRelaciones.setTitulo("Bot con Relaciones");
        botConRelaciones.setDescripcion("Descripción del bot con relaciones");

        // Crear funciones
        Funcion funcion = new Funcion();
        funcion.setDescripcion("Función de prueba");
        botConRelaciones.setFunciones(Arrays.asList(funcion));

        // Crear integraciones
        Integracion integracion = new Integracion();
        integracion.setNombre("Integración de prueba");
        botConRelaciones.setIntegraciones(Arrays.asList(integracion));

        when(botServicioRepository.save(any(BotServicio.class))).thenReturn(botConRelaciones);

        // Act
        BotServicio resultado = botServicioService.crear(botConRelaciones);

        // Assert
        assertNotNull(resultado);
        assertEquals("Bot con Relaciones", resultado.getTitulo());
        assertNotNull(resultado.getFunciones());
        assertEquals(1, resultado.getFunciones().size());
        assertNotNull(resultado.getIntegraciones());
        assertEquals(1, resultado.getIntegraciones().size());
        
        // Verificar que las relaciones tienen la referencia correcta al bot padre
        assertEquals(botConRelaciones, resultado.getFunciones().get(0).getBotServicio());
        assertEquals(botConRelaciones, resultado.getIntegraciones().get(0).getBotServicio());
        
        verify(botServicioRepository, times(1)).save(botConRelaciones);
    }

    @Test
    void testActualizar_Existe() {
        // Arrange
        BotServicio botExistente = new BotServicio();
        botExistente.setId(1L);
        botExistente.setTitulo("Bot Original");
        botExistente.setDescripcion("Descripción original");

        BotServicio botActualizado = new BotServicio();
        botActualizado.setTitulo("Bot Actualizado");
        botActualizado.setDescripcion("Descripción actualizada");
        botActualizado.setImagenUrl("http://example.com/updated.jpg");

        when(botServicioRepository.findById(1L)).thenReturn(Optional.of(botExistente));
        when(botServicioRepository.save(any(BotServicio.class))).thenReturn(botExistente);

        // Act
        BotServicio resultado = botServicioService.actualizar(1L, botActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Bot Actualizado", resultado.getTitulo());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        assertEquals("http://example.com/updated.jpg", resultado.getImagenUrl());
        
        verify(botServicioRepository, times(1)).findById(1L);
        verify(botServicioRepository, times(1)).save(botExistente);
    }

    @Test
    void testActualizar_NoExiste() {
        // Arrange
        BotServicio botActualizado = new BotServicio();
        botActualizado.setTitulo("Bot Actualizado");

        when(botServicioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            botServicioService.actualizar(999L, botActualizado);
        });

        verify(botServicioRepository, times(1)).findById(999L);
        verify(botServicioRepository, never()).save(any());
    }

    @Test
    void testActualizar_ConRelaciones() {
        // Arrange
        BotServicio botExistente = new BotServicio();
        botExistente.setId(1L);
        botExistente.setTitulo("Bot Original");
        botExistente.setDescripcion("Descripción original");

        // Agregar relaciones existentes
        Funcion funcionExistente = new Funcion();
        funcionExistente.setDescripcion("Función existente");
        botExistente.setFunciones(Arrays.asList(funcionExistente));

        BotServicio botActualizado = new BotServicio();
        botActualizado.setTitulo("Bot Actualizado");
        botActualizado.setDescripcion("Descripción actualizada");

        // Crear nuevas relaciones
        Funcion nuevaFuncion = new Funcion();
        nuevaFuncion.setDescripcion("Nueva función");
        botActualizado.setFunciones(Arrays.asList(nuevaFuncion));

        Integracion nuevaIntegracion = new Integracion();
        nuevaIntegracion.setNombre("Nueva integración");
        botActualizado.setIntegraciones(Arrays.asList(nuevaIntegracion));

        when(botServicioRepository.findById(1L)).thenReturn(Optional.of(botExistente));
        when(botServicioRepository.save(any(BotServicio.class))).thenReturn(botExistente);

        // Act
        BotServicio resultado = botServicioService.actualizar(1L, botActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Bot Actualizado", resultado.getTitulo());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        
        // Verificar que las relaciones fueron actualizadas
        assertNotNull(resultado.getFunciones());
        assertEquals(1, resultado.getFunciones().size());
        assertEquals("Nueva función", resultado.getFunciones().get(0).getDescripcion());
        
        assertNotNull(resultado.getIntegraciones());
        assertEquals(1, resultado.getIntegraciones().size());
        assertEquals("Nueva integración", resultado.getIntegraciones().get(0).getNombre());
        
        // Verificar que las relaciones tienen la referencia correcta al bot padre
        assertEquals(botExistente, resultado.getFunciones().get(0).getBotServicio());
        assertEquals(botExistente, resultado.getIntegraciones().get(0).getBotServicio());
        
        verify(botServicioRepository, times(1)).findById(1L);
        verify(botServicioRepository, times(1)).save(botExistente);
    }

    @Test
    void testEliminar() {
        // Arrange
        Long id = 1L;
        doNothing().when(botServicioRepository).deleteById(id);

        // Act
        botServicioService.eliminar(id);

        // Assert
        verify(botServicioRepository, times(1)).deleteById(id);
    }
} 