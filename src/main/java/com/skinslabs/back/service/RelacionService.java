package com.skinslabs.back.service;

import com.skinslabs.back.model.*;
import com.skinslabs.back.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para agregar relaciones a un BotServicio de forma individual (funciones, integraciones, etc.).
 * Garantiza la integridad referencial y el manejo transaccional.
 */
@Service
public class RelacionService {
    private final BotServicioRepository botServicioRepository;
    private final FuncionRepository funcionRepository;
    private final IntegracionRepository integracionRepository;
    private final CasoUsoRepository casoUsoRepository;
    private final TecnologiaRepository tecnologiaRepository;
    private final FlujoAutomatizadoRepository flujoAutomatizadoRepository;
    private final RequisitoRepository requisitoRepository;
    private static final String BOT_NO_ENCONTRADO = "Bot no encontrado";

    public RelacionService(BotServicioRepository botServicioRepository,
                          FuncionRepository funcionRepository,
                          IntegracionRepository integracionRepository,
                          CasoUsoRepository casoUsoRepository,
                          TecnologiaRepository tecnologiaRepository,
                          FlujoAutomatizadoRepository flujoAutomatizadoRepository,
                          RequisitoRepository requisitoRepository) {
        this.botServicioRepository = botServicioRepository;
        this.funcionRepository = funcionRepository;
        this.integracionRepository = integracionRepository;
        this.casoUsoRepository = casoUsoRepository;
        this.tecnologiaRepository = tecnologiaRepository;
        this.flujoAutomatizadoRepository = flujoAutomatizadoRepository;
        this.requisitoRepository = requisitoRepository;
    }

    /**
     * Agrega una función a un bot existente.
     */
    @Transactional
    public Funcion agregarFuncion(Long botId, Funcion funcion) {
        BotServicio bot = botServicioRepository.findById(botId).orElseThrow(() -> new RuntimeException(BOT_NO_ENCONTRADO));
        funcion.setBotServicio(bot);
        return funcionRepository.save(funcion);
    }
    /**
     * Agrega una integración a un bot existente.
     */
    @Transactional
    public Integracion agregarIntegracion(Long botId, Integracion integracion) {
        BotServicio bot = botServicioRepository.findById(botId).orElseThrow(() -> new RuntimeException(BOT_NO_ENCONTRADO));
        integracion.setBotServicio(bot);
        return integracionRepository.save(integracion);
    }
    /**
     * Agrega un caso de uso a un bot existente.
     */
    @Transactional
    public CasoUso agregarCasoUso(Long botId, CasoUso casoUso) {
        BotServicio bot = botServicioRepository.findById(botId).orElseThrow(() -> new RuntimeException(BOT_NO_ENCONTRADO));
        casoUso.setBotServicio(bot);
        return casoUsoRepository.save(casoUso);
    }
    /**
     * Agrega una tecnología a un bot existente.
     */
    @Transactional
    public Tecnologia agregarTecnologia(Long botId, Tecnologia tecnologia) {
        BotServicio bot = botServicioRepository.findById(botId).orElseThrow(() -> new RuntimeException(BOT_NO_ENCONTRADO));
        tecnologia.setBotServicio(bot);
        return tecnologiaRepository.save(tecnologia);
    }
    /**
     * Agrega un flujo automatizado a un bot existente.
     */
    @Transactional
    public FlujoAutomatizado agregarFlujoAutomatizado(Long botId, FlujoAutomatizado flujo) {
        BotServicio bot = botServicioRepository.findById(botId).orElseThrow(() -> new RuntimeException(BOT_NO_ENCONTRADO));
        flujo.setBotServicio(bot);
        return flujoAutomatizadoRepository.save(flujo);
    }
    /**
     * Agrega un requisito a un bot existente.
     */
    @Transactional
    public Requisito agregarRequisito(Long botId, Requisito requisito) {
        BotServicio bot = botServicioRepository.findById(botId).orElseThrow(() -> new RuntimeException(BOT_NO_ENCONTRADO));
        requisito.setBotServicio(bot);
        return requisitoRepository.save(requisito);
    }
} 