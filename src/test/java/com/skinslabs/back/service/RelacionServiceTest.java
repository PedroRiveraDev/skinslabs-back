package com.skinslabs.back.service;

import com.skinslabs.back.model.*;
import com.skinslabs.back.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class RelacionServiceTest {

    private RelacionService relacionService;
    private BotServicioRepository botServicioRepository;
    private FuncionRepository funcionRepository;
    private IntegracionRepository integracionRepository;
    private CasoUsoRepository casoUsoRepository;
    private TecnologiaRepository tecnologiaRepository;
    private FlujoAutomatizadoRepository flujoAutomatizadoRepository;
    private RequisitoRepository requisitoRepository;

    @BeforeEach
    void setUp() {
        botServicioRepository = mock(BotServicioRepository.class);
        funcionRepository = mock(FuncionRepository.class);
        integracionRepository = mock(IntegracionRepository.class);
        casoUsoRepository = mock(CasoUsoRepository.class);
        tecnologiaRepository = mock(TecnologiaRepository.class);
        flujoAutomatizadoRepository = mock(FlujoAutomatizadoRepository.class);
        requisitoRepository = mock(RequisitoRepository.class);

        relacionService = new RelacionService(
            botServicioRepository,
            funcionRepository,
            integracionRepository,
            casoUsoRepository,
            tecnologiaRepository,
            flujoAutomatizadoRepository,
            requisitoRepository
        );
    }

    @Test
    void testAgregarFuncion_Exito() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        bot.setTitulo("Test Bot");

        Funcion funcion = new Funcion();
        funcion.setDescripcion("Test Function");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));
        when(funcionRepository.save(any(Funcion.class))).thenReturn(funcion);

        // Act
        Funcion result = relacionService.agregarFuncion(botId, funcion);

        // Assert
        assertNotNull(result);
        assertEquals(bot, result.getBotServicio());
        assertEquals("Test Function", result.getDescripcion());

        verify(botServicioRepository).findById(botId);
        verify(funcionRepository).save(any(Funcion.class));
    }

    @Test
    void testAgregarFuncion_BotNoEncontrado() {
        // Arrange
        Long botId = 1L;
        Funcion funcion = new Funcion();
        funcion.setDescripcion("Test Function");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            relacionService.agregarFuncion(botId, funcion);
        });

        assertEquals("Bot no encontrado", exception.getMessage());
        verify(botServicioRepository).findById(botId);
        verify(funcionRepository, never()).save(any(Funcion.class));
    }

    @Test
    void testAgregarIntegracion_Exito() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        bot.setTitulo("Test Bot");

        Integracion integracion = new Integracion();
        integracion.setNombre("Test Integration");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));
        when(integracionRepository.save(any(Integracion.class))).thenReturn(integracion);

        // Act
        Integracion result = relacionService.agregarIntegracion(botId, integracion);

        // Assert
        assertNotNull(result);
        assertEquals(bot, result.getBotServicio());
        assertEquals("Test Integration", result.getNombre());

        verify(botServicioRepository).findById(botId);
        verify(integracionRepository).save(any(Integracion.class));
    }

    @Test
    void testAgregarIntegracion_BotNoEncontrado() {
        // Arrange
        Long botId = 1L;
        Integracion integracion = new Integracion();
        integracion.setNombre("Test Integration");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            relacionService.agregarIntegracion(botId, integracion);
        });

        assertEquals("Bot no encontrado", exception.getMessage());
        verify(botServicioRepository).findById(botId);
        verify(integracionRepository, never()).save(any(Integracion.class));
    }

    @Test
    void testAgregarCasoUso_Exito() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        bot.setTitulo("Test Bot");

        CasoUso casoUso = new CasoUso();
        casoUso.setDescripcion("Test Case");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));
        when(casoUsoRepository.save(any(CasoUso.class))).thenReturn(casoUso);

        // Act
        CasoUso result = relacionService.agregarCasoUso(botId, casoUso);

        // Assert
        assertNotNull(result);
        assertEquals(bot, result.getBotServicio());
        assertEquals("Test Case", result.getDescripcion());

        verify(botServicioRepository).findById(botId);
        verify(casoUsoRepository).save(any(CasoUso.class));
    }

    @Test
    void testAgregarCasoUso_BotNoEncontrado() {
        // Arrange
        Long botId = 1L;
        CasoUso casoUso = new CasoUso();
        casoUso.setDescripcion("Test Case");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            relacionService.agregarCasoUso(botId, casoUso);
        });

        assertEquals("Bot no encontrado", exception.getMessage());
        verify(botServicioRepository).findById(botId);
        verify(casoUsoRepository, never()).save(any(CasoUso.class));
    }

    @Test
    void testAgregarTecnologia_Exito() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        bot.setTitulo("Test Bot");

        Tecnologia tecnologia = new Tecnologia();
        tecnologia.setNombre("Test Technology");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));
        when(tecnologiaRepository.save(any(Tecnologia.class))).thenReturn(tecnologia);

        // Act
        Tecnologia result = relacionService.agregarTecnologia(botId, tecnologia);

        // Assert
        assertNotNull(result);
        assertEquals(bot, result.getBotServicio());
        assertEquals("Test Technology", result.getNombre());

        verify(botServicioRepository).findById(botId);
        verify(tecnologiaRepository).save(any(Tecnologia.class));
    }

    @Test
    void testAgregarTecnologia_BotNoEncontrado() {
        // Arrange
        Long botId = 1L;
        Tecnologia tecnologia = new Tecnologia();
        tecnologia.setNombre("Test Technology");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            relacionService.agregarTecnologia(botId, tecnologia);
        });

        assertEquals("Bot no encontrado", exception.getMessage());
        verify(botServicioRepository).findById(botId);
        verify(tecnologiaRepository, never()).save(any(Tecnologia.class));
    }

    @Test
    void testAgregarFlujoAutomatizado_Exito() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        bot.setTitulo("Test Bot");

        FlujoAutomatizado flujo = new FlujoAutomatizado();
        flujo.setDescripcion("Test Flow");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));
        when(flujoAutomatizadoRepository.save(any(FlujoAutomatizado.class))).thenReturn(flujo);

        // Act
        FlujoAutomatizado result = relacionService.agregarFlujoAutomatizado(botId, flujo);

        // Assert
        assertNotNull(result);
        assertEquals(bot, result.getBotServicio());
        assertEquals("Test Flow", result.getDescripcion());

        verify(botServicioRepository).findById(botId);
        verify(flujoAutomatizadoRepository).save(any(FlujoAutomatizado.class));
    }

    @Test
    void testAgregarFlujoAutomatizado_BotNoEncontrado() {
        // Arrange
        Long botId = 1L;
        FlujoAutomatizado flujo = new FlujoAutomatizado();
        flujo.setDescripcion("Test Flow");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            relacionService.agregarFlujoAutomatizado(botId, flujo);
        });

        assertEquals("Bot no encontrado", exception.getMessage());
        verify(botServicioRepository).findById(botId);
        verify(flujoAutomatizadoRepository, never()).save(any(FlujoAutomatizado.class));
    }

    @Test
    void testAgregarRequisito_Exito() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);
        bot.setTitulo("Test Bot");

        Requisito requisito = new Requisito();
        requisito.setDescripcion("Test Requirement");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));
        when(requisitoRepository.save(any(Requisito.class))).thenReturn(requisito);

        // Act
        Requisito result = relacionService.agregarRequisito(botId, requisito);

        // Assert
        assertNotNull(result);
        assertEquals(bot, result.getBotServicio());
        assertEquals("Test Requirement", result.getDescripcion());

        verify(botServicioRepository).findById(botId);
        verify(requisitoRepository).save(any(Requisito.class));
    }

    @Test
    void testAgregarRequisito_BotNoEncontrado() {
        // Arrange
        Long botId = 1L;
        Requisito requisito = new Requisito();
        requisito.setDescripcion("Test Requirement");

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            relacionService.agregarRequisito(botId, requisito);
        });

        assertEquals("Bot no encontrado", exception.getMessage());
        verify(botServicioRepository).findById(botId);
        verify(requisitoRepository, never()).save(any(Requisito.class));
    }

    @Test
    void testAgregarFuncion_ConFuncionNula() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            relacionService.agregarFuncion(botId, null);
        });
    }

    @Test
    void testAgregarIntegracion_ConIntegracionNula() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            relacionService.agregarIntegracion(botId, null);
        });
    }

    @Test
    void testAgregarCasoUso_ConCasoUsoNulo() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            relacionService.agregarCasoUso(botId, null);
        });
    }

    @Test
    void testAgregarTecnologia_ConTecnologiaNula() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            relacionService.agregarTecnologia(botId, null);
        });
    }

    @Test
    void testAgregarFlujoAutomatizado_ConFlujoNulo() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            relacionService.agregarFlujoAutomatizado(botId, null);
        });
    }

    @Test
    void testAgregarRequisito_ConRequisitoNulo() {
        // Arrange
        Long botId = 1L;
        BotServicio bot = new BotServicio();
        bot.setId(botId);

        when(botServicioRepository.findById(botId)).thenReturn(java.util.Optional.of(bot));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            relacionService.agregarRequisito(botId, null);
        });
    }
} 