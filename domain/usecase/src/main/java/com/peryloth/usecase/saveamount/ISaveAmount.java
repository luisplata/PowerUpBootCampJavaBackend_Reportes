package com.peryloth.usecase.saveamount;

import com.peryloth.model.reporte.Reporte;
import reactor.core.publisher.Mono;

public interface ISaveAmount {
    Mono<Reporte> saveLoan(Reporte body);
}
