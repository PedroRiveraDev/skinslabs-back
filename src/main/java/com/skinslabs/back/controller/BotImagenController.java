package com.skinslabs.back.controller;

import com.skinslabs.back.model.BotServicio;
import com.skinslabs.back.repository.BotServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/bots")
public class BotImagenController {

    private final BotServicioRepository botServicioRepository;
    private static final String BOT_NO_ENCONTRADO = "Bot no encontrado";

    public BotImagenController(BotServicioRepository botServicioRepository) {
        this.botServicioRepository = botServicioRepository;
    }

    /**
     * Endpoint para subir una imagen y asociarla a un bot.
     * Guarda el archivo en /uploads y actualiza el campo imagenUrl del bot.
     */
    @PostMapping("/{id}/imagen")
    public ResponseEntity<String> subirImagen(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        BotServicio bot = botServicioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, BOT_NO_ENCONTRADO));

        // Guardar archivo en /uploads en la raíz del proyecto
        String uploadsDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
        File uploadsFolder = new File(uploadsDir);
        if (!uploadsFolder.exists()) uploadsFolder.mkdirs();

        String fileName = "bot_" + id + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dest = new File(uploadsDir + fileName);
        file.transferTo(dest);

        // Actualizar el bot con la URL de la imagen
        bot.setImagenUrl("/uploads/" + fileName);
        botServicioRepository.save(bot);

        return ResponseEntity.ok(bot.getImagenUrl());
    }
} 