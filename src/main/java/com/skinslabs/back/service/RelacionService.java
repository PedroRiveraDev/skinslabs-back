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
    @Autowired private BotServicioRepository botServicioRepository;
    @Autowired private FuncionRepository funcionRepository;
    @Autowired private IntegracionRepository integracionRepository;
    @Autowired private CasoUsoRepository casoUsoRepository;
    @Autowired private TecnologiaRepository tecnologiaRepository;
    @Autowired private FlujoAutomatizadoRepository flujoAutomatizadoRepository;
    @Autowired private RequisitoRepository requisitoRepository;

    /**
     * Agrega una función a un bot existente.
     */
    @Transactional
    public Funcion agregarFuncion(Long botId, Funcion funcion) {
        BotServicio bot = botServicioRepository.findById(botId).orElseThrow(() -> new RuntimeException("Bot no encontrado"));
        funcion.setBotServicio(bot);
        return funcionRepository.save(funcion);
    }
    /**
     * Agrega una integración a un bot existente.
     */
    @Transactional
    public Integracion agregarIntegracion(Long botId, Integracion integracion) {
        BotServicio bot = botServicioRepository.findById(botId).orElseThrow(() -> new RuntimeException("Bot no encontrado"));
        integracion.setBotServicio(bot);
        return integracionRepository.save(integracion);
    }
    /**
     * Agrega un caso de uso a un bot existente.
     */
    @Transactional
    public CasoUso agregarCasoUso(Long botId, CasoUso casoUso) {
        BotServicio bot = botServicioRepository.findById(botId).orElseThrow(() -> new RuntimeException("Bot no encontrado"));
        casoUso.setBotServicio(bot);
        return casoUsoRepository.save(casoUso);
    }
    /**
     * Agrega una tecnología a un bot existente.
     */
    @Transactional
    public Tecnologia agregarTecnologia(Long botId, Tecnologia tecnologia) {
        BotServicio bot = botServicioRepository.findById(botId).orElseThrow(() -> new RuntimeException("Bot no encontrado"));
        tecnologia.setBotServicio(bot);
        return tecnologiaRepository.save(tecnologia);
    }
    /**
     * Agrega un flujo automatizado a un bot existente.
     */
    @Transactional
    public FlujoAutomatizado agregarFlujoAutomatizado(Long botId, FlujoAutomatizado flujo) {
        BotServicio bot = botServicioRepository.findById(botId).orElseThrow(() -> new RuntimeException("Bot no encontrado"));
        flujo.setBotServicio(bot);
        return flujoAutomatizadoRepository.save(flujo);
    }
    /**
     * Agrega un requisito a un bot existente.
     */
    @Transactional
    public Requisito agregarRequisito(Long botId, Requisito requisito) {
        BotServicio bot = botServicioRepository.findById(botId).orElseThrow(() -> new RuntimeException("Bot no encontrado"));
        requisito.setBotServicio(bot);
        return requisitoRepository.save(requisito);
    }
} 