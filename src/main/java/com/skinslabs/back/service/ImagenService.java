package com.skinslabs.back.service;

import com.skinslabs.back.model.BotServicio;
import com.skinslabs.back.repository.BotServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ImagenService {

    private final BotServicioRepository botServicioRepository;
    private static final String UPLOADS_DIR = "uploads";
    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    public ImagenService(BotServicioRepository botServicioRepository) {
        this.botServicioRepository = botServicioRepository;
    }

    /**
     * Sube una imagen para un bot específico
     */
    public String subirImagenParaBot(Long botId, MultipartFile file) throws IOException {
        // Validar que el bot existe
        BotServicio bot = botServicioRepository.findById(botId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bot no encontrado"));

        // Validar archivo
        validarArchivo(file);

        // Crear directorio si no existe
        crearDirectorioUploads();

        // Generar nombre único para el archivo
        String fileName = generarNombreArchivo(botId, file);
        File dest = new File(getUploadsPath() + fileName);

        try {
            // Guardar el archivo
            file.transferTo(dest);
            
            // Actualizar el bot con la URL de la imagen
            String imagenUrl = "/uploads/" + fileName;
            bot.setImagenUrl(imagenUrl);
            botServicioRepository.save(bot);

            return imagenUrl;
        } catch (IOException e) {
            // Si hay error al guardar, eliminar el archivo si se creó
            if (dest.exists()) {
                if (!dest.delete()) {
                    System.err.println("[WARN] No se pudo eliminar el archivo temporal tras error de subida: " + dest.getAbsolutePath());
                }
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al guardar la imagen: " + e.getMessage());
        }
    }

    /**
     * Elimina la imagen de un bot
     */
    public void eliminarImagenDeBot(Long botId) {
        BotServicio bot = botServicioRepository.findById(botId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bot no encontrado"));

        String imagenUrl = bot.getImagenUrl();
        if (imagenUrl == null || imagenUrl.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El bot no tiene imagen asociada");
        }

        // Extraer el nombre del archivo de la URL
        String fileName = imagenUrl.substring(imagenUrl.lastIndexOf("/") + 1);
        File imageFile = new File(getUploadsPath() + fileName);

        // Eliminar el archivo físico si existe
        if (imageFile.exists()) {
            if (!imageFile.delete()) {
                // Manejar el valor booleano retornado por delete
                System.err.println("[WARN] No se pudo eliminar el archivo de imagen: " + imageFile.getAbsolutePath());
                // No lanzar excepción, solo advertir
            }
        }

        // Limpiar la URL de imagen del bot
        bot.setImagenUrl(null);
        botServicioRepository.save(bot);
    }

    /**
     * Valida que el archivo sea una imagen válida
     */
    private void validarArchivo(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se proporcionó ningún archivo");
        }

        if (!TIPOS_PERMITIDOS.contains(file.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Tipo de archivo no permitido. Solo se permiten: JPEG, JPG, PNG, GIF, WEBP");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "El archivo es demasiado grande. Máximo 10MB permitido");
        }
    }

    /**
     * Crea el directorio de uploads si no existe
     */
    private void crearDirectorioUploads() {
        File uploadsFolder = new File(getUploadsPath());
        if (!uploadsFolder.exists()) {
            if (!uploadsFolder.mkdirs()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "No se pudo crear el directorio de uploads");
            }
        }
    }

    /**
     * Genera un nombre único para el archivo
     */
    private String generarNombreArchivo(Long botId, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        return "bot_" + botId + "_" + System.currentTimeMillis() + extension;
    }

    /**
     * Obtiene la ruta completa del directorio uploads
     */
    private String getUploadsPath() {
        return System.getProperty("user.dir") + File.separator + UPLOADS_DIR + File.separator;
    }

    /**
     * Verifica si una imagen existe
     */
    public boolean imagenExiste(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }

        // Validar el nombre del archivo para evitar path traversal
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            return false;
        }

        File file = new File(getUploadsPath() + filename);
        return file.exists() && file.isFile();
    }
} 