package com.bancobase.bootcamp.dto;

import com.bancobase.bootcamp.dto.response.Symbol;
import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class CurrenciesDTO {
    private String name;
    private String symbol;
    private Double value;

    public static CurrenciesDTO getFromSchema(String symbol, Double value, Map<String, Symbol> symbols) {
        String name = symbols.get(symbol).getDescription();

        return CurrenciesDTO.builder()
                .name(name)
                .symbol(symbol)
                .value(value)
                .build();
    }
}