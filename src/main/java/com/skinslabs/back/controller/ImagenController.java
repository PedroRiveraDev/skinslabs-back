package com.skinslabs.back.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/imagenes")
@CrossOrigin(origins = "http://localhost:3000")
public class ImagenController {

    private static final String UPLOADS_DIR = "uploads";

    /**
     * Endpoint para servir imágenes desde la carpeta uploads
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> servirImagen(@PathVariable String filename) {
        try {
            // Validar el nombre del archivo para evitar path traversal
            if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre de archivo inválido");
            }

            // Construir la ruta del archivo
            String uploadsPath = System.getProperty("user.dir") + File.separator + UPLOADS_DIR;
            Path filePath = Paths.get(uploadsPath, filename);
            File file = filePath.toFile();

            // Verificar que el archivo existe
            if (!file.exists() || !file.isFile()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada");
            }

            // Verificar que el archivo está dentro del directorio uploads
            Path uploadsDir = Paths.get(uploadsPath).toAbsolutePath().normalize();
            Path normalizedFilePath = filePath.toAbsolutePath().normalize();
            if (!normalizedFilePath.startsWith(uploadsDir)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
            }

            // Determinar el tipo de contenido
            String contentType = determinarTipoContenido(filename);
            
            // Crear el recurso
            Resource resource = new FileSystemResource(file);

            // Configurar headers para cache y CORS
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setCacheControl("public, max-age=31536000"); // Cache por 1 año
            headers.set("Access-Control-Allow-Origin", "*");
            headers.set("Access-Control-Allow-Methods", "GET");
            headers.set("Access-Control-Allow-Headers", "*");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al servir la imagen: " + e.getMessage());
        }
    }

    /**
     * Determina el tipo de contenido basado en la extensión del archivo
     */
    private String determinarTipoContenido(String filename) {
        String extension = "";
        if (filename.contains(".")) {
            extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        }

        return switch (extension) {
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".png" -> "image/png";
            case ".gif" -> "image/gif";
            case ".webp" -> "image/webp";
            case ".bmp" -> "image/bmp";
            case ".svg" -> "image/svg+xml";
            default -> "application/octet-stream";
        };
    }

    /**
     * Endpoint para verificar si una imagen existe
     */
    @GetMapping("/existe/{filename:.+}")
    public ResponseEntity<Boolean> imagenExiste(@PathVariable String filename) {
        try {
            // Validar el nombre del archivo
            if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
            }

            String uploadsPath = System.getProperty("user.dir") + File.separator + UPLOADS_DIR;
            Path filePath = Paths.get(uploadsPath, filename);
            File file = filePath.toFile();

            // Verificar que el archivo existe y está dentro del directorio uploads
            Path uploadsDir = Paths.get(uploadsPath).toAbsolutePath().normalize();
            Path normalizedFilePath = filePath.toAbsolutePath().normalize();
            
            boolean existe = file.exists() && file.isFile() && normalizedFilePath.startsWith(uploadsDir);
            if (existe) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
} 