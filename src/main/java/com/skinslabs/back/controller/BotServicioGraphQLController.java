package com.skinslabs.back.controller;

import com.skinslabs.back.model.*;
import com.skinslabs.back.service.BotServicioService;
import com.skinslabs.back.service.RelacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Resolver GraphQL para exponer queries y mutaciones de BotServicio y sus relaciones.
 * Permite obtener, crear, actualizar y eliminar bots, así como agregar relaciones por separado.
 */
@Controller
public class BotServicioGraphQLController {
    @Autowired
    private BotServicioService botServicioService;
    @Autowired
    private RelacionService relacionService;

    /**
     * Query para obtener todos los bots con sus relaciones.
     */
    @QueryMapping
    public List<BotServicio> obtenerBots() {
        return botServicioService.obtenerTodos();
    }

    /**
     * Query para obtener un bot por su ID.
     */
    @QueryMapping
    public BotServicio obtenerBotPorId(@Argument Long id) {
        return botServicioService.obtenerPorId(id).orElse(null);
    }

    /**
     * Mutación para crear un nuevo bot con relaciones.
     */
    @MutationMapping
    public BotServicio crearBot(@Argument BotServicio input) {
        return botServicioService.crear(input);
    }

    /**
     * Mutación para actualizar un bot existente y sus relaciones.
     */
    @MutationMapping
    public BotServicio actualizarBot(@Argument Long id, @Argument BotServicio input) {
        return botServicioService.actualizar(id, input);
    }

    /**
     * Mutación para eliminar un bot por su ID.
     */
    @MutationMapping
    public Boolean eliminarBot(@Argument Long id) {
        botServicioService.eliminar(id);
        return true;
    }

    @MutationMapping
    public Funcion agregarFuncion(@Argument Long botId, @Argument Funcion input) {
        return relacionService.agregarFuncion(botId, input);
    }
    @MutationMapping
    public Integracion agregarIntegracion(@Argument Long botId, @Argument Integracion input) {
        return relacionService.agregarIntegracion(botId, input);
    }
    @MutationMapping
    public CasoUso agregarCasoUso(@Argument Long botId, @Argument CasoUso input) {
        return relacionService.agregarCasoUso(botId, input);
    }
    @MutationMapping
    public Tecnologia agregarTecnologia(@Argument Long botId, @Argument Tecnologia input) {
        return relacionService.agregarTecnologia(botId, input);
    }
    @MutationMapping
    public FlujoAutomatizado agregarFlujoAutomatizado(@Argument Long botId, @Argument FlujoAutomatizado input) {
        return relacionService.agregarFlujoAutomatizado(botId, input);
    }
    @MutationMapping
    public Requisito agregarRequisito(@Argument Long botId, @Argument Requisito input) {
        return relacionService.agregarRequisito(botId, input);
    }
} 