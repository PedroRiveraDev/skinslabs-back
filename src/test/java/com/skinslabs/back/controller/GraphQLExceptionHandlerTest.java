package com.skinslabs.back.controller;

import graphql.GraphQLError;
import graphql.language.Field;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import graphql.execution.ExecutionStepInfo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GraphQLExceptionHandlerTest {

    @Mock
    private DataFetchingEnvironment env;

    @Mock
    private Field field;

    @Mock
    private ExecutionStepInfo executionStepInfo;

    private GraphQLExceptionHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new GraphQLExceptionHandler();
        // Configurar el mock de DataFetchingEnvironment
        when(env.getField()).thenReturn(field);
        when(field.getSourceLocation()).thenReturn(new SourceLocation(1, 1));
        when(env.getExecutionStepInfo()).thenReturn(executionStepInfo);
        when(executionStepInfo.getPath()).thenReturn(null); // No es relevante para el test
    }

    @Test
    void testResolveToSingleErrorWithConstraintViolationException() {
        // Crear una ConstraintViolationException mock
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        
        when(violation.getMessage()).thenReturn("Campo requerido");
        violations.add(violation);
        when(ex.getConstraintViolations()).thenReturn(violations);

        GraphQLError error = handler.resolveToSingleError(ex, env);

        assertNotNull(error);
        assertTrue(error.getMessage().contains("Error de validación: Campo requerido"));
    }

    @Test
    void testResolveToSingleErrorWithMethodArgumentNotValidException() {
        // Crear una MethodArgumentNotValidException mock
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        WebExchangeBindException bindException = mock(WebExchangeBindException.class);
        
        when(ex.getBindingResult()).thenReturn(bindException);
        when(bindException.getAllErrors()).thenReturn(java.util.List.of(
            new org.springframework.validation.FieldError("field", "defaultMessage", "Error de validación")
        ));

        GraphQLError error = handler.resolveToSingleError(ex, env);

        assertNotNull(error);
        assertTrue(error.getMessage().contains("Error de validación: Error de validación"));
    }

    @Test
    void testResolveToSingleErrorWithGenericException() {
        RuntimeException ex = new RuntimeException("Error interno del servidor");

        GraphQLError error = handler.resolveToSingleError(ex, env);

        assertNotNull(error);
        assertTrue(error.getMessage().contains("Error interno: Error interno del servidor"));
    }
} 