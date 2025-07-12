package com.skinslabs.back.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
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
    public List<Funcion> getFunciones() { return funciones; }
    public void setFunciones(List<Funcion> funciones) { this.funciones = funciones; }
    public List<Integracion> getIntegraciones() { return integraciones; }
    public void setIntegraciones(List<Integracion> integraciones) { this.integraciones = integraciones; }
    public List<CasoUso> getCasosUso() { return casosUso; }
    public void setCasosUso(List<CasoUso> casosUso) { this.casosUso = casosUso; }
    public List<Tecnologia> getTecnologias() { return tecnologias; }
    public void setTecnologias(List<Tecnologia> tecnologias) { this.tecnologias = tecnologias; }
    public List<FlujoAutomatizado> getFlujosAutomatizados() { return flujosAutomatizados; }
    public void setFlujosAutomatizados(List<FlujoAutomatizado> flujosAutomatizados) { this.flujosAutomatizados = flujosAutomatizados; }
    public List<Requisito> getRequisitos() { return requisitos; }
    public void setRequisitos(List<Requisito> requisitos) { this.requisitos = requisitos; }
} 