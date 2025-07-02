package com.skinslabs.back.controller;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import jakarta.validation.ConstraintViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) ex;
            return GraphqlErrorBuilder.newError(env)
                    .message("Error de validación: " + cve.getConstraintViolations().iterator().next().getMessage())
                    .build();
        }
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException manve = (MethodArgumentNotValidException) ex;
            String msg = manve.getBindingResult().getAllErrors().get(0).getDefaultMessage();
            return GraphqlErrorBuilder.newError(env)
                    .message("Error de validación: " + msg)
                    .build();
        }
        return GraphqlErrorBuilder.newError(env)
                .message("Error interno: " + ex.getMessage())
                .build();
    }
} 