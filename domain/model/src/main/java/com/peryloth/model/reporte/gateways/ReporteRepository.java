package com.peryloth.model.reporte.gateways;

import com.peryloth.model.reporte.Reporte;
import reactor.core.publisher.Mono;

public interface ReporteRepository {
    Mono<Void> saveR(Reporte r);
}
