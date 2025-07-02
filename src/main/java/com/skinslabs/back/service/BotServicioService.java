package com.skinslabs.back.service;

import com.skinslabs.back.model.*;
import com.skinslabs.back.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de bots y sus relaciones.
 * Incluye lógica para crear, actualizar y eliminar bots, asegurando la integridad de las relaciones.
 */
@Service
public class BotServicioService {
    @Autowired
    private BotServicioRepository botServicioRepository;

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
    public BotServicio actualizar(Long id, BotServicio botActualizado) {
        BotServicio bot = botServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bot no encontrado"));
        bot.setTitulo(botActualizado.getTitulo());
        bot.setDescripcion(botActualizado.getDescripcion());
        bot.setImagenUrl(botActualizado.getImagenUrl());

        // Limpiar y agregar nuevas relaciones
        bot.getFunciones().clear();
        if (botActualizado.getFunciones() != null) {
            botActualizado.getFunciones().forEach(f -> {
                f.setBotServicio(bot);
                bot.getFunciones().add(f);
            });
        }
        bot.getIntegraciones().clear();
        if (botActualizado.getIntegraciones() != null) {
            botActualizado.getIntegraciones().forEach(i -> {
                i.setBotServicio(bot);
                bot.getIntegraciones().add(i);
            });
        }
        bot.getCasosUso().clear();
        if (botActualizado.getCasosUso() != null) {
            botActualizado.getCasosUso().forEach(c -> {
                c.setBotServicio(bot);
                bot.getCasosUso().add(c);
            });
        }
        bot.getTecnologias().clear();
        if (botActualizado.getTecnologias() != null) {
            botActualizado.getTecnologias().forEach(t -> {
                t.setBotServicio(bot);
                bot.getTecnologias().add(t);
            });
        }
        bot.getFlujosAutomatizados().clear();
        if (botActualizado.getFlujosAutomatizados() != null) {
            botActualizado.getFlujosAutomatizados().forEach(f -> {
                f.setBotServicio(bot);
                bot.getFlujosAutomatizados().add(f);
            });
        }
        bot.getRequisitos().clear();
        if (botActualizado.getRequisitos() != null) {
            botActualizado.getRequisitos().forEach(r -> {
                r.setBotServicio(bot);
                bot.getRequisitos().add(r);
            });
        }
        return botServicioRepository.save(bot);
    }

    public void eliminar(Long id) {
        botServicioRepository.deleteById(id);
    }
} 