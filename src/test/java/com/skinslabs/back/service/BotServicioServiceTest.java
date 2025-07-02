package com.skinslabs.back.service;

import com.skinslabs.back.model.BotServicio;
import com.skinslabs.back.repository.BotServicioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BotServicioServiceTest {

    @Mock
    private BotServicioRepository botServicioRepository;

    @InjectMocks
    private BotServicioService botServicioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearBotServicio_exito() {
        BotServicio bot = new BotServicio();
        bot.setTitulo("Bot de ejemplo");
        bot.setDescripcion("Un bot para pruebas");
        when(botServicioRepository.save(any(BotServicio.class))).thenReturn(bot);

        BotServicio resultado = botServicioService.crear(bot);

        assertThat(resultado.getTitulo()).isEqualTo("Bot de ejemplo");
        assertThat(resultado.getDescripcion()).isEqualTo("Un bot para pruebas");
        verify(botServicioRepository, times(1)).save(bot);
    }

    @Test
    void testObtenerTodosBots() {
        BotServicio bot1 = new BotServicio();
        bot1.setTitulo("Bot 1");
        BotServicio bot2 = new BotServicio();
        bot2.setTitulo("Bot 2");
        when(botServicioRepository.findAll()).thenReturn(Arrays.asList(bot1, bot2));

        List<BotServicio> bots = botServicioService.obtenerTodos();

        assertThat(bots).hasSize(2);
        assertThat(bots.get(0).getTitulo()).isEqualTo("Bot 1");
        assertThat(bots.get(1).getTitulo()).isEqualTo("Bot 2");
    }

    @Test
    void testObtenerBotPorId_encontrado() {
        BotServicio bot = new BotServicio();
        bot.setId(1L);
        when(botServicioRepository.findById(1L)).thenReturn(Optional.of(bot));

        Optional<BotServicio> resultado = botServicioService.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }

    @Test
    void testObtenerBotPorId_noEncontrado() {
        when(botServicioRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<BotServicio> resultado = botServicioService.obtenerPorId(2L);

        assertThat(resultado).isNotPresent();
    }

    @Test
    void testEliminarBot() {
        doNothing().when(botServicioRepository).deleteById(1L);
        botServicioService.eliminar(1L);
        verify(botServicioRepository, times(1)).deleteById(1L);
    }
} 