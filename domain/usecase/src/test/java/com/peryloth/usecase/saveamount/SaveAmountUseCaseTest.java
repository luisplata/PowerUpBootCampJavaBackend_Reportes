package com.peryloth.usecase.saveamount;

import com.peryloth.model.reporte.Reporte;
import com.peryloth.model.reporte.gateways.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class SaveAmountUseCaseTest {

    private ReporteRepository reporteRepository;
    private SaveAmountUseCase useCase;

    @BeforeEach
    void setUp() {
        reporteRepository = Mockito.mock(ReporteRepository.class);
        useCase = new SaveAmountUseCase(reporteRepository);
    }

    @Test
    void shouldSaveLoanSuccessfully() {
        // Arrange
        Reporte input = new Reporte("aprobado", 100L, 10.0);
        Reporte expected = new Reporte("aprobado", 101L, 10.0);

        when(reporteRepository.incrementCounter("approvedLoans", 1L, input.totalAmount))
                .thenReturn(Mono.just(expected));

        // Act & Assert
        StepVerifier.create(useCase.saveLoan(input))
                .expectNext(expected)
                .verifyComplete();

        verify(reporteRepository, times(1))
                .incrementCounter("approvedLoans", 1L, input.totalAmount);
    }

    @Test
    void shouldReturnEmptyWhenRepositoryReturnsEmpty() {
        Reporte input = new Reporte("aprobado", 100L, 10.0);

        when(reporteRepository.incrementCounter("approvedLoans", 1L, input.totalAmount))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.saveLoan(input))
                .verifyComplete();

        verify(reporteRepository, times(1))
                .incrementCounter("approvedLoans", 1L, input.totalAmount);
    }

    @Test
    void shouldPropagateErrorWhenRepositoryFails() {
        Reporte input = new Reporte("aprobado", 100L, 10.0);
        RuntimeException error = new RuntimeException("DB error");

        when(reporteRepository.incrementCounter("approvedLoans", 1L, input.totalAmount))
                .thenReturn(Mono.error(error));

        StepVerifier.create(useCase.saveLoan(input))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("DB error"))
                .verify();

        verify(reporteRepository, times(1))
                .incrementCounter("approvedLoans", 1L, input.totalAmount);
    }
}
