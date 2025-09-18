package com.peryloth.sqsreader.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peryloth.model.reporte.Reporte;
import com.peryloth.sqsreader.Dto.MessageFromSqsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanEventMapper {
    private final ObjectMapper objectMapper;

    public Reporte toDomain(String messageBody) {
        try {
            var dto = objectMapper.readValue(messageBody, MessageFromSqsDto.class);
            return new Reporte("approvedLoans", 1L, dto.totalAmount());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("❌ Error parseando mensaje SQS", e);
        }
    }
}
