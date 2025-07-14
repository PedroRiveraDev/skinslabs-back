package com.skinslabs.back.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Configuración personalizada para health checks
 */
@Component
public class HealthConfig implements HealthIndicator {

    @Override
    public Health health() {
        try {
            // Verificar espacio en disco
            File uploadsDir = new File("uploads");
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs();
            }
            
            long freeSpace = uploadsDir.getFreeSpace();
            long totalSpace = uploadsDir.getTotalSpace();
            double usagePercent = ((double) (totalSpace - freeSpace) / totalSpace) * 100;
            
            if (usagePercent > 90) {
                return Health.down()
                    .withDetail("disk_usage", String.format("%.2f%%", usagePercent))
                    .withDetail("free_space", formatBytes(freeSpace))
                    .withDetail("message", "Espacio en disco crítico")
                    .build();
            }
            
            // Verificar directorio de datos
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            return Health.up()
                .withDetail("disk_usage", String.format("%.2f%%", usagePercent))
                .withDetail("free_space", formatBytes(freeSpace))
                .withDetail("uploads_dir", uploadsDir.getAbsolutePath())
                .withDetail("data_dir", dataDir.getAbsolutePath())
                .withDetail("java_version", System.getProperty("java.version"))
                .withDetail("memory_usage", getMemoryUsage())
                .build();
                
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    private String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        double usagePercent = ((double) usedMemory / totalMemory) * 100;
        
        return String.format("%.2f%% (%s / %s)", 
            usagePercent, 
            formatBytes(usedMemory), 
            formatBytes(totalMemory));
    }
} 