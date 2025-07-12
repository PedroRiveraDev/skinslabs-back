package com.skinslabs.back.controller;

import com.skinslabs.back.service.ImagenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/bots")
@CrossOrigin(origins = "http://localhost:3000")
public class BotImagenController {

    private final ImagenService imagenService;

    public BotImagenController(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    /**
     * Endpoint para subir una imagen y asociarla a un bot.
     */
    @PostMapping("/{id}/imagen")
    public ResponseEntity<String> subirImagen(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        String imagenUrl = imagenService.subirImagenParaBot(id, file);
        return ResponseEntity.ok(imagenUrl);
    }

    /**
     * Endpoint para eliminar la imagen de un bot
     */
    @DeleteMapping("/{id}/imagen")
    public ResponseEntity<String> eliminarImagen(@PathVariable Long id) {
        imagenService.eliminarImagenDeBot(id);
        return ResponseEntity.ok("Imagen eliminada correctamente");
    }
} 