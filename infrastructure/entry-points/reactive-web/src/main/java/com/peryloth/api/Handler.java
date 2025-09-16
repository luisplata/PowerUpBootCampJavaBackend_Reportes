package com.peryloth.api;

import com.peryloth.model.reporte.Reporte;
import com.peryloth.model.reporte.gateways.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ReporteRepository reporteRepository;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return reporteRepository.saveR(new Reporte("hoy", "otroavez"))
                .then(ServerResponse.ok().bodyValue("Guardado correctamente"));
    }
}
