package com.skinslabs.back.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skinslabs.back.service.BotsCatalogo;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class BotsCatalogoController {
    @GetMapping("/catalogo")
    public List<BotsCatalogo> getCatalogo() {
        return List.of(
                new BotsCatalogo(
                        "Bot de Reportes Automatizados",
                        "Genera y envía reportes con métricas clave semanal o mensualmente a través de Gmail o SendGrid."),
                new BotsCatalogo(
                        "Bot de Resumen de Conversaciones",
                        "Analiza conversaciones extensas y genera resúmenes automáticos utilizando GPT, ideal para auditoría."
                ),
                new BotsCatalogo(
                        " Bot Integrador de CRM",
                        "Conecta automáticamente con HubSpot, Notion o Trello para registrar leads, tareas o contactos de clientes."
                        ),
                new BotsCatalogo(
                        "Bot de Atención 24/7",
                        "Chatbot basado en GPT-4 que responde preguntas frecuentes, deriva solicitudes y automatiza respuestas por correo o WhatsApp."
                        ));
    }
}
