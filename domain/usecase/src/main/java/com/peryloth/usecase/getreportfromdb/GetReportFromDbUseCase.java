package com.peryloth.usecase.getreportfromdb;

import com.peryloth.model.reporte.Reporte;
import com.peryloth.model.reporte.gateways.ReporteRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetReportFromDbUseCase implements IGetReportFromDb {

    private final ReporteRepository reporteRepository;

    @Override
    public Mono<Reporte> getData() {
        return reporteRepository.getApprovedTotal();
    }
}
