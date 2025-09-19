package com.peryloth.api;

import com.peryloth.model.reporte.Reporte;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    private final AuthFilter authFilter;

    public RouterRest(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/reportes",
                    beanClass = Handler.class,
                    beanMethod = "listenGETUseCase",
                    operation = @Operation(
                            operationId = "getReportes",
                            summary = "Obtener reportes aprobados",
                            description = "Obtiene el total de reportes aprobados desde la base de datos",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Reporte obtenido correctamente",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = Reporte.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Token inválido o no provisto",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = String.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = String.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/v1/reportes"), handler::listenGETUseCase)
                .filter(authFilter);
    }
}
