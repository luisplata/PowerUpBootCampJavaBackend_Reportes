package com.peryloth.usecase.saveamount;

import com.peryloth.model.reporte.Reporte;
import com.peryloth.model.reporte.gateways.ReporteRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SaveAmountUseCase implements ISaveAmount {

    private final ReporteRepository dynamoDbRepository;

    @Override
    public Mono<Reporte> saveLoan(Reporte body) {
        return dynamoDbRepository.incrementCounter("approvedLoans", 1L, body.totalAmount);
    }
}
