package com.peryloth.usecase.getreportfromdb;

import com.peryloth.model.reporte.Reporte;
import com.peryloth.model.reporte.gateways.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class GetReportFromDbUseCaseTest {

    private ReporteRepository reporteRepository;
    private GetReportFromDbUseCase useCase;

    @BeforeEach
    void setUp() {
        reporteRepository = Mockito.mock(ReporteRepository.class);
        useCase = new GetReportFromDbUseCase(reporteRepository);
    }

    @Test
    void shouldReturnReporteWhenRepositoryReturnsData() {
        // Arrange
        Reporte expected = new Reporte("aprobado", 1L, 10.0);
        when(reporteRepository.getApprovedTotal()).thenReturn(Mono.just(expected));

        // Act & Assert
        StepVerifier.create(useCase.getData())
                .expectNext(expected)
                .verifyComplete();

        verify(reporteRepository, times(1)).getApprovedTotal();
    }

    @Test
    void shouldReturnEmptyWhenRepositoryReturnsEmpty() {
        when(reporteRepository.getApprovedTotal()).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getData())
                .verifyComplete();

        verify(reporteRepository, times(1)).getApprovedTotal();
    }

    @Test
    void shouldPropagateErrorWhenRepositoryFails() {
        RuntimeException error = new RuntimeException("DB error");
        when(reporteRepository.getApprovedTotal()).thenReturn(Mono.error(error));

        StepVerifier.create(useCase.getData())
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("DB error"))
                .verify();

        verify(reporteRepository, times(1)).getApprovedTotal();
    }
}
