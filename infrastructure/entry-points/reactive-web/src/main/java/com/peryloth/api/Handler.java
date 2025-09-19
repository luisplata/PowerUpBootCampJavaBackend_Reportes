package com.peryloth.api;

import com.peryloth.api.Dto.ResponseGetTotalAmountDto;
import com.peryloth.model.reporte.Reporte;
import com.peryloth.model.reporte.gateways.ReporteRepository;
import com.peryloth.usecase.getreportfromdb.IGetReportFromDb;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final IGetReportFromDb reportFromDb;
    private final AmountMapper mapper;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return reportFromDb.getData()
                .flatMap(reporte -> Mono.just(new ResponseGetTotalAmountDto(reporte.count, reporte.totalAmount)))
                .flatMap(reporteDto -> ServerResponse.ok().bodyValue(mapper.toJson(reporteDto)));
    }
}
