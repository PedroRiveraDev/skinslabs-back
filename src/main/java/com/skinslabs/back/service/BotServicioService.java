package com.skinslabs.back.service;

import com.skinslabs.back.model.*;
import com.skinslabs.back.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

/**
 * Servicio para la gestión de bots y sus relaciones.
 * Incluye lógica para crear, actualizar y eliminar bots, asegurando la integridad de las relaciones.
 */
@Service
public class BotServicioService {
    private final BotServicioRepository botServicioRepository;
    private static final String BOT_NO_ENCONTRADO = "Bot no encontrado";

    public BotServicioService(BotServicioRepository botServicioRepository) {
        this.botServicioRepository = botServicioRepository;
    }

    public List<BotServicio> obtenerTodos() {
        return botServicioRepository.findAll();
    }

    public Optional<BotServicio> obtenerPorId(Long id) {
        return botServicioRepository.findById(id);
    }

    /**
     * Crea un nuevo bot y asegura que todas las relaciones tengan la referencia correcta al bot padre.
     */
    public BotServicio crear(BotServicio bot) {
        if (bot.getFunciones() != null) {
            bot.getFunciones().forEach(f -> f.setBotServicio(bot));
        }
        if (bot.getIntegraciones() != null) {
            bot.getIntegraciones().forEach(i -> i.setBotServicio(bot));
        }
        if (bot.getCasosUso() != null) {
            bot.getCasosUso().forEach(c -> c.setBotServicio(bot));
        }
        if (bot.getTecnologias() != null) {
            bot.getTecnologias().forEach(t -> t.setBotServicio(bot));
        }
        if (bot.getFlujosAutomatizados() != null) {
            bot.getFlujosAutomatizados().forEach(f -> f.setBotServicio(bot));
        }
        if (bot.getRequisitos() != null) {
            bot.getRequisitos().forEach(r -> r.setBotServicio(bot));
        }
        bot.setImagenUrl(bot.getImagenUrl());
        return botServicioRepository.save(bot);
    }

    /**
     * Actualiza un bot existente y reemplaza todas sus relaciones de forma segura.
     */
    @Transactional
    public BotServicio actualizar(Long id, BotServicio botActualizado) {
        BotServicio bot = botServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(BOT_NO_ENCONTRADO));
        
        // Actualizar campos básicos
        bot.setTitulo(botActualizado.getTitulo());
        bot.setDescripcion(botActualizado.getDescripcion());
        bot.setImagenUrl(botActualizado.getImagenUrl());

        // Actualizar relaciones usando los setters mejorados
        bot.setFunciones(botActualizado.getFunciones());
        bot.setIntegraciones(botActualizado.getIntegraciones());
        bot.setCasosUso(botActualizado.getCasosUso());
        bot.setTecnologias(botActualizado.getTecnologias());
        bot.setFlujosAutomatizados(botActualizado.getFlujosAutomatizados());
        bot.setRequisitos(botActualizado.getRequisitos());

        return botServicioRepository.save(bot);
    }



    public void eliminar(Long id) {
        botServicioRepository.deleteById(id);
    }
} 