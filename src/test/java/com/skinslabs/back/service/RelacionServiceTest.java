package com.skinslabs.back.service;

import com.skinslabs.back.model.*;
import com.skinslabs.back.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RelacionServiceTest {
    @Mock private BotServicioRepository botServicioRepository;
    @Mock private FuncionRepository funcionRepository;
    @Mock private IntegracionRepository integracionRepository;
    @Mock private CasoUsoRepository casoUsoRepository;
    @Mock private TecnologiaRepository tecnologiaRepository;
    @Mock private FlujoAutomatizadoRepository flujoAutomatizadoRepository;
    @Mock private RequisitoRepository requisitoRepository;

    @InjectMocks private RelacionService relacionService;

    private BotServicio bot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bot = new BotServicio();
        bot.setId(1L);
    }

    @Test
    void testAgregarFuncion_exito() {
        Funcion funcion = new Funcion();
        funcion.setDescripcion("Función de prueba");
        when(botServicioRepository.findById(1L)).thenReturn(Optional.of(bot));
        when(funcionRepository.save(any(Funcion.class))).thenAnswer(i -> i.getArgument(0));

        Funcion resultado = relacionService.agregarFuncion(1L, funcion);

        assertThat(resultado.getDescripcion()).isEqualTo("Función de prueba");
        assertThat(resultado.getBotServicio()).isEqualTo(bot);
        verify(funcionRepository).save(funcion);
    }

    @Test
    void testAgregarIntegracion_exito() {
        Integracion integracion = new Integracion();
        integracion.setNombre("Integración X");
        when(botServicioRepository.findById(1L)).thenReturn(Optional.of(bot));
        when(integracionRepository.save(any(Integracion.class))).thenAnswer(i -> i.getArgument(0));

        Integracion resultado = relacionService.agregarIntegracion(1L, integracion);

        assertThat(resultado.getNombre()).isEqualTo("Integración X");
        assertThat(resultado.getBotServicio()).isEqualTo(bot);
        verify(integracionRepository).save(integracion);
    }

    @Test
    void testAgregarCasoUso_exito() {
        CasoUso caso = new CasoUso();
        caso.setDescripcion("Caso de uso");
        when(botServicioRepository.findById(1L)).thenReturn(Optional.of(bot));
        when(casoUsoRepository.save(any(CasoUso.class))).thenAnswer(i -> i.getArgument(0));

        CasoUso resultado = relacionService.agregarCasoUso(1L, caso);

        assertThat(resultado.getDescripcion()).isEqualTo("Caso de uso");
        assertThat(resultado.getBotServicio()).isEqualTo(bot);
        verify(casoUsoRepository).save(caso);
    }

    @Test
    void testAgregarTecnologia_exito() {
        Tecnologia tecnologia = new Tecnologia();
        tecnologia.setNombre("Java");
        when(botServicioRepository.findById(1L)).thenReturn(Optional.of(bot));
        when(tecnologiaRepository.save(any(Tecnologia.class))).thenAnswer(i -> i.getArgument(0));

        Tecnologia resultado = relacionService.agregarTecnologia(1L, tecnologia);

        assertThat(resultado.getNombre()).isEqualTo("Java");
        assertThat(resultado.getBotServicio()).isEqualTo(bot);
        verify(tecnologiaRepository).save(tecnologia);
    }

    @Test
    void testAgregarFlujoAutomatizado_exito() {
        FlujoAutomatizado flujo = new FlujoAutomatizado();
        flujo.setDescripcion("Flujo 1");
        when(botServicioRepository.findById(1L)).thenReturn(Optional.of(bot));
        when(flujoAutomatizadoRepository.save(any(FlujoAutomatizado.class))).thenAnswer(i -> i.getArgument(0));

        FlujoAutomatizado resultado = relacionService.agregarFlujoAutomatizado(1L, flujo);

        assertThat(resultado.getDescripcion()).isEqualTo("Flujo 1");
        assertThat(resultado.getBotServicio()).isEqualTo(bot);
        verify(flujoAutomatizadoRepository).save(flujo);
    }

    @Test
    void testAgregarRequisito_exito() {
        Requisito req = new Requisito();
        req.setDescripcion("Requisito 1");
        when(botServicioRepository.findById(1L)).thenReturn(Optional.of(bot));
        when(requisitoRepository.save(any(Requisito.class))).thenAnswer(i -> i.getArgument(0));

        Requisito resultado = relacionService.agregarRequisito(1L, req);

        assertThat(resultado.getDescripcion()).isEqualTo("Requisito 1");
        assertThat(resultado.getBotServicio()).isEqualTo(bot);
        verify(requisitoRepository).save(req);
    }

    @Test
    void testAgregarFuncion_botNoExiste() {
        Funcion funcion = new Funcion();
        when(botServicioRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> relacionService.agregarFuncion(2L, funcion));
    }
} 