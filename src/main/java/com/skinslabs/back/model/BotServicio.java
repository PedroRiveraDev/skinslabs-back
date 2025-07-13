package com.skinslabs.back.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidad principal que representa un bot de automatización en el catálogo de SkinsLabs.
 * Incluye relaciones a funcionalidades, integraciones, casos de uso, tecnologías, flujos automatizados y requisitos.
 */
@Entity
public class BotServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Título del bot */
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String titulo;

    /** Descripción del bot */
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 5, max = 255, message = "La descripción debe tener entre 5 y 255 caracteres")
    private String descripcion;

    /** URL o path de la imagen asociada al bot */
    private String imagenUrl;

    @OneToMany(mappedBy = "botServicio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Funcion> funciones = new ArrayList<>();

    @OneToMany(mappedBy = "botServicio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Integracion> integraciones = new ArrayList<>();

    @OneToMany(mappedBy = "botServicio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CasoUso> casosUso = new ArrayList<>();

    @OneToMany(mappedBy = "botServicio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Tecnologia> tecnologias = new ArrayList<>();

    @OneToMany(mappedBy = "botServicio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<FlujoAutomatizado> flujosAutomatizados = new ArrayList<>();

    @OneToMany(mappedBy = "botServicio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Requisito> requisitos = new ArrayList<>();

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    
    // Getters para las colecciones
    public List<Funcion> getFunciones() { 
        if (funciones == null) funciones = new ArrayList<>();
        return funciones; 
    }
    public List<Integracion> getIntegraciones() { 
        if (integraciones == null) integraciones = new ArrayList<>();
        return integraciones; 
    }
    public List<CasoUso> getCasosUso() { 
        if (casosUso == null) casosUso = new ArrayList<>();
        return casosUso; 
    }
    public List<Tecnologia> getTecnologias() { 
        if (tecnologias == null) tecnologias = new ArrayList<>();
        return tecnologias; 
    }
    public List<FlujoAutomatizado> getFlujosAutomatizados() { 
        if (flujosAutomatizados == null) flujosAutomatizados = new ArrayList<>();
        return flujosAutomatizados; 
    }
    public List<Requisito> getRequisitos() { 
        if (requisitos == null) requisitos = new ArrayList<>();
        return requisitos; 
    }
    
    // Setters para las colecciones (mantener la referencia a la misma lista)
    public void setFunciones(List<Funcion> funciones) { 
        this.funciones.clear();
        if (funciones != null) {
            funciones.forEach(f -> {
                f.setBotServicio(this);
                this.funciones.add(f);
            });
        }
    }
    public void setIntegraciones(List<Integracion> integraciones) { 
        this.integraciones.clear();
        if (integraciones != null) {
            integraciones.forEach(i -> {
                i.setBotServicio(this);
                this.integraciones.add(i);
            });
        }
    }
    public void setCasosUso(List<CasoUso> casosUso) { 
        this.casosUso.clear();
        if (casosUso != null) {
            casosUso.forEach(c -> {
                c.setBotServicio(this);
                this.casosUso.add(c);
            });
        }
    }
    public void setTecnologias(List<Tecnologia> tecnologias) { 
        this.tecnologias.clear();
        if (tecnologias != null) {
            tecnologias.forEach(t -> {
                t.setBotServicio(this);
                this.tecnologias.add(t);
            });
        }
    }
    public void setFlujosAutomatizados(List<FlujoAutomatizado> flujosAutomatizados) { 
        this.flujosAutomatizados.clear();
        if (flujosAutomatizados != null) {
            flujosAutomatizados.forEach(f -> {
                f.setBotServicio(this);
                this.flujosAutomatizados.add(f);
            });
        }
    }
    public void setRequisitos(List<Requisito> requisitos) { 
        this.requisitos.clear();
        if (requisitos != null) {
            requisitos.forEach(r -> {
                r.setBotServicio(this);
                this.requisitos.add(r);
            });
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotServicio that = (BotServicio) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BotServicio{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                '}';
    }
} 