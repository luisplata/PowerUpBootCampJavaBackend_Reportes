package com.peryloth.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peryloth.api.Dto.ResponseGetTotalAmountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AmountMapper {
    private final ObjectMapper objectMapper; // Spring ya lo inyecta por defecto

    public String toJson(ResponseGetTotalAmountDto event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("❌ Error serializando LoanEvent", e);
        }
    }
}
