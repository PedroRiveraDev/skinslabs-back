package com.skinslabs.back.repository;

import com.skinslabs.back.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BotServicioRepositoryTest {
    @Autowired
    private BotServicioRepository botServicioRepository;
    @Autowired
    private FuncionRepository funcionRepository;
    @Autowired
    private IntegracionRepository integracionRepository;
    @Autowired
    private CasoUsoRepository casoUsoRepository;
    @Autowired
    private TecnologiaRepository tecnologiaRepository;
    @Autowired
    private FlujoAutomatizadoRepository flujoAutomatizadoRepository;
    @Autowired
    private RequisitoRepository requisitoRepository;

    @Test
    void testGuardarYRecuperarBot() {
        BotServicio bot = new BotServicio();
        bot.setTitulo("Bot Persistencia");
        bot.setDescripcion("Bot para test de JPA");
        bot.setImagenUrl("/uploads/test.jpg");
        BotServicio guardado = botServicioRepository.save(bot);
        BotServicio recuperado = botServicioRepository.findById(guardado.getId()).orElseThrow();
        assertThat(recuperado.getTitulo()).isEqualTo("Bot Persistencia");
        assertThat(recuperado.getImagenUrl()).isEqualTo("/uploads/test.jpg");
    }

    @Test
    void testPersistenciaRelaciones() {
        BotServicio bot = new BotServicio();
        bot.setTitulo("Bot Relacional");
        bot.setDescripcion("Bot con relaciones");

        Funcion funcion = new Funcion();
        funcion.setDescripcion("Función 1");
        funcion.setBotServicio(bot);
        bot.setFunciones(List.of(funcion));

        Integracion integracion = new Integracion();
        integracion.setNombre("Integración 1");
        integracion.setBotServicio(bot);
        bot.setIntegraciones(List.of(integracion));

        CasoUso caso = new CasoUso();
        caso.setDescripcion("Caso 1");
        caso.setBotServicio(bot);
        bot.setCasosUso(List.of(caso));

        Tecnologia tecnologia = new Tecnologia();
        tecnologia.setNombre("Java");
        tecnologia.setBotServicio(bot);
        bot.setTecnologias(List.of(tecnologia));

        FlujoAutomatizado flujo = new FlujoAutomatizado();
        flujo.setDescripcion("Flujo 1");
        flujo.setBotServicio(bot);
        bot.setFlujosAutomatizados(List.of(flujo));

        Requisito req = new Requisito();
        req.setDescripcion("Req 1");
        req.setBotServicio(bot);
        bot.setRequisitos(List.of(req));

        BotServicio guardado = botServicioRepository.save(bot);
        BotServicio recuperado = botServicioRepository.findById(guardado.getId()).orElseThrow();
        assertThat(recuperado.getFunciones()).hasSize(1);
        assertThat(recuperado.getIntegraciones()).hasSize(1);
        assertThat(recuperado.getCasosUso()).hasSize(1);
        assertThat(recuperado.getTecnologias()).hasSize(1);
        assertThat(recuperado.getFlujosAutomatizados()).hasSize(1);
        assertThat(recuperado.getRequisitos()).hasSize(1);
    }
} 