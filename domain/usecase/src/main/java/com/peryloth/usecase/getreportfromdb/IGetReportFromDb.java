package com.peryloth.usecase.getreportfromdb;

import com.peryloth.model.reporte.Reporte;
import reactor.core.publisher.Mono;

public interface IGetReportFromDb {
    Mono<Reporte> getData();
}
