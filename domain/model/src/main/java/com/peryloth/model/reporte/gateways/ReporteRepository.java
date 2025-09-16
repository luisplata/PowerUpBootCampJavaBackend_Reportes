package com.peryloth.model.reporte.gateways;

import com.peryloth.model.reporte.Reporte;
import reactor.core.publisher.Mono;

public interface ReporteRepository {
    Mono<Reporte> incrementCounter(String counterId, Long amountToAdd, Double totalAmountToAdd);
    Mono<Reporte> getApprovedTotal();
}
