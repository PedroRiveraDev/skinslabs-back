package com.skinslabs.back.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.Arguments;

class BotServicioTest {

    private BotServicio botServicio;
    private Validator validator;

    @BeforeEach
    void setUp() {
        botServicio = new BotServicio();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long id = 1L;
        String titulo = "Bot de Prueba";
        String descripcion = "Descripción del bot de prueba";
        String imagenUrl = "http://example.com/image.jpg";

        // Act
        botServicio.setId(id);
        botServicio.setTitulo(titulo);
        botServicio.setDescripcion(descripcion);
        botServicio.setImagenUrl(imagenUrl);

        // Assert
        assertEquals(id, botServicio.getId());
        assertEquals(titulo, botServicio.getTitulo());
        assertEquals(descripcion, botServicio.getDescripcion());
        assertEquals(imagenUrl, botServicio.getImagenUrl());
    }

    @ParameterizedTest
    @MethodSource("relacionesProvider")
    <T> void testRelaciones(String tipo, T relacion, java.util.function.BiConsumer<BotServicio, List<T>> setter, java.util.function.Function<BotServicio, List<T>> getter, java.util.function.Function<T, String> extractor, String expectedDescripcion) {
        // Act
        setter.accept(botServicio, List.of(relacion));
        // Assert
        List<T> lista = getter.apply(botServicio);
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(expectedDescripcion, extractor.apply(lista.get(0)));
    }

    static Stream<Arguments> relacionesProvider() {
        Funcion funcion = new Funcion();
        funcion.setDescripcion("Función de prueba");
        Integracion integracion = new Integracion();
        integracion.setNombre("Integración de prueba");
        CasoUso casoUso = new CasoUso();
        casoUso.setDescripcion("Caso de uso de prueba");
        Tecnologia tecnologia = new Tecnologia();
        tecnologia.setNombre("Tecnología de prueba");
        FlujoAutomatizado flujo = new FlujoAutomatizado();
        flujo.setDescripcion("Flujo de prueba");
        Requisito requisito = new Requisito();
        requisito.setDescripcion("Requisito de prueba");
        return Stream.of(
            Arguments.of("Funcion", funcion, (java.util.function.BiConsumer<BotServicio, List<Funcion>>) BotServicio::setFunciones, (java.util.function.Function<BotServicio, List<Funcion>>) BotServicio::getFunciones, (java.util.function.Function<Funcion, String>) Funcion::getDescripcion, "Función de prueba"),
            Arguments.of("Integracion", integracion, (java.util.function.BiConsumer<BotServicio, List<Integracion>>) BotServicio::setIntegraciones, (java.util.function.Function<BotServicio, List<Integracion>>) BotServicio::getIntegraciones, (java.util.function.Function<Integracion, String>) Integracion::getNombre, "Integración de prueba"),
            Arguments.of("CasoUso", casoUso, (java.util.function.BiConsumer<BotServicio, List<CasoUso>>) BotServicio::setCasosUso, (java.util.function.Function<BotServicio, List<CasoUso>>) BotServicio::getCasosUso, (java.util.function.Function<CasoUso, String>) CasoUso::getDescripcion, "Caso de uso de prueba"),
            Arguments.of("Tecnologia", tecnologia, (java.util.function.BiConsumer<BotServicio, List<Tecnologia>>) BotServicio::setTecnologias, (java.util.function.Function<BotServicio, List<Tecnologia>>) BotServicio::getTecnologias, (java.util.function.Function<Tecnologia, String>) Tecnologia::getNombre, "Tecnología de prueba"),
            Arguments.of("FlujoAutomatizado", flujo, (java.util.function.BiConsumer<BotServicio, List<FlujoAutomatizado>>) BotServicio::setFlujosAutomatizados, (java.util.function.Function<BotServicio, List<FlujoAutomatizado>>) BotServicio::getFlujosAutomatizados, (java.util.function.Function<FlujoAutomatizado, String>) FlujoAutomatizado::getDescripcion, "Flujo de prueba"),
            Arguments.of("Requisito", requisito, (java.util.function.BiConsumer<BotServicio, List<Requisito>>) BotServicio::setRequisitos, (java.util.function.Function<BotServicio, List<Requisito>>) BotServicio::getRequisitos, (java.util.function.Function<Requisito, String>) Requisito::getDescripcion, "Requisito de prueba")
        );
    }

    @Test
    void testValidacionTituloObligatorio() {
        // Arrange
        botServicio.setTitulo("");
        botServicio.setDescripcion("Descripción válida");

        // Act
        Set<ConstraintViolation<BotServicio>> violations = validator.validate(botServicio);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")));
    }

    @Test
    void testValidacionTituloTamanioMinimo() {
        // Arrange
        botServicio.setTitulo("AB");
        botServicio.setDescripcion("Descripción válida");

        // Act
        Set<ConstraintViolation<BotServicio>> violations = validator.validate(botServicio);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")));
    }

    @Test
    void testValidacionTituloTamanioMaximo() {
        // Arrange
        botServicio.setTitulo("A".repeat(101));
        botServicio.setDescripcion("Descripción válida");

        // Act
        Set<ConstraintViolation<BotServicio>> violations = validator.validate(botServicio);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")));
    }

    @Test
    void testValidacionDescripcionObligatoria() {
        // Arrange
        botServicio.setTitulo("Título válido");
        botServicio.setDescripcion("");

        // Act
        Set<ConstraintViolation<BotServicio>> violations = validator.validate(botServicio);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")));
    }

    @Test
    void testValidacionDescripcionTamanioMinimo() {
        // Arrange
        botServicio.setTitulo("Título válido");
        botServicio.setDescripcion("1234");

        // Act
        Set<ConstraintViolation<BotServicio>> violations = validator.validate(botServicio);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")));
    }

    @Test
    void testValidacionDescripcionTamanioMaximo() {
        // Arrange
        botServicio.setTitulo("Título válido");
        botServicio.setDescripcion("A".repeat(256));

        // Act
        Set<ConstraintViolation<BotServicio>> violations = validator.validate(botServicio);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")));
    }

    @Test
    void testValidacionExitoso() {
        // Arrange
        botServicio.setTitulo("Título válido");
        botServicio.setDescripcion("Descripción válida con más de 5 caracteres");

        // Act
        Set<ConstraintViolation<BotServicio>> violations = validator.validate(botServicio);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        BotServicio bot1 = new BotServicio();
        bot1.setId(1L);
        bot1.setTitulo("Bot 1");
        bot1.setDescripcion("Descripción 1");

        BotServicio bot2 = new BotServicio();
        bot2.setId(1L);
        bot2.setTitulo("Bot 1");
        bot2.setDescripcion("Descripción 1");

        BotServicio bot3 = new BotServicio();
        bot3.setId(2L);
        bot3.setTitulo("Bot 2");
        bot3.setDescripcion("Descripción 2");

        // Act & Assert
        assertEquals(bot1, bot2);
        assertNotEquals(bot1, bot3);
        assertEquals(bot1.hashCode(), bot2.hashCode());
        assertNotEquals(bot1.hashCode(), bot3.hashCode());
    }

    @Test
    void testEqualsWithNull() {
        // Arrange
        BotServicio bot = new BotServicio();
        bot.setId(1L);

        // Act & Assert
        assertNotEquals(null, bot);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Arrange
        BotServicio bot = new BotServicio();
        bot.setId(1L);
        Object other = new Object();

        // Act & Assert
        assertNotEquals(bot, other);
    }

    @Test
    void testEqualsWithSameObject() {
        // Arrange
        BotServicio bot = new BotServicio();
        bot.setId(1L);

        // Act & Assert
        assertEquals(bot, bot);
    }

    @Test
    void testEqualsWithNullId() {
        // Arrange
        BotServicio bot1 = new BotServicio();
        bot1.setId(null);
        bot1.setTitulo("Bot 1");

        BotServicio bot2 = new BotServicio();
        bot2.setId(null);
        bot2.setTitulo("Bot 1");

        BotServicio bot3 = new BotServicio();
        bot3.setId(1L);
        bot3.setTitulo("Bot 1");

        // Act & Assert
        assertEquals(bot1, bot2);
        assertNotEquals(bot1, bot3);
    }

    @Test
    void testToString() {
        // Arrange
        BotServicio bot = new BotServicio();
        bot.setId(1L);
        bot.setTitulo("Test Bot");
        bot.setDescripcion("Test Description");

        // Act
        String result = bot.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Test Bot"));
        assertTrue(result.contains("Test Description"));
    }

    @Test
    void testRelacionesNull() {
        // Arrange
        BotServicio bot = new BotServicio();

        // Act & Assert - Verificar que las listas se inicializan correctamente
        assertNotNull(bot.getFunciones());
        assertNotNull(bot.getIntegraciones());
        assertNotNull(bot.getCasosUso());
        assertNotNull(bot.getTecnologias());
        assertNotNull(bot.getFlujosAutomatizados());
        assertNotNull(bot.getRequisitos());
    }

    @Test
    void testRelacionesEmpty() {
        // Arrange
        BotServicio bot = new BotServicio();

        // Act & Assert
        assertTrue(bot.getFunciones().isEmpty());
        assertTrue(bot.getIntegraciones().isEmpty());
        assertTrue(bot.getCasosUso().isEmpty());
        assertTrue(bot.getTecnologias().isEmpty());
        assertTrue(bot.getFlujosAutomatizados().isEmpty());
        assertTrue(bot.getRequisitos().isEmpty());
    }

    @Test
    void testRelacionesMultiple() {
        // Arrange
        BotServicio bot = new BotServicio();
        Funcion funcion1 = new Funcion();
        funcion1.setDescripcion("Función 1");
        Funcion funcion2 = new Funcion();
        funcion2.setDescripcion("Función 2");

        // Act
        bot.setFunciones(List.of(funcion1, funcion2));

        // Assert
        assertEquals(2, bot.getFunciones().size());
        assertEquals("Función 1", bot.getFunciones().get(0).getDescripcion());
        assertEquals("Función 2", bot.getFunciones().get(1).getDescripcion());
    }

    @Test
    void testImagenUrlNull() {
        // Arrange
        BotServicio bot = new BotServicio();
        bot.setImagenUrl(null);

        // Act & Assert
        assertNull(bot.getImagenUrl());
    }

    @Test
    void testImagenUrlEmpty() {
        // Arrange
        BotServicio bot = new BotServicio();
        bot.setImagenUrl("");

        // Act & Assert
        assertEquals("", bot.getImagenUrl());
    }

    @Test
    void testValidacionTituloNull() {
        // Arrange
        botServicio.setTitulo(null);
        botServicio.setDescripcion("Descripción válida");

        // Act
        Set<ConstraintViolation<BotServicio>> violations = validator.validate(botServicio);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("titulo")));
    }

    @Test
    void testValidacionDescripcionNull() {
        // Arrange
        botServicio.setTitulo("Título válido");
        botServicio.setDescripcion(null);

        // Act
        Set<ConstraintViolation<BotServicio>> violations = validator.validate(botServicio);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")));
    }
} 