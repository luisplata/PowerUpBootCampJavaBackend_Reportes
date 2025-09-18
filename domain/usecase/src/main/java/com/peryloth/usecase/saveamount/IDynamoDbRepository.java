package com.peryloth.usecase.saveamount;

import com.peryloth.model.reporte.Reporte;
import reactor.core.publisher.Mono;

public interface IDynamoDbRepository {
    Mono<Reporte> saveReporte(Reporte reporte);
}
