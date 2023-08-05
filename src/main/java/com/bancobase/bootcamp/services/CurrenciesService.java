package com.bancobase.bootcamp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bancobase.bootcamp.dto.CurrenciesDTO;
import com.bancobase.bootcamp.dto.response.ExchangeRateResponse;
import com.bancobase.bootcamp.dto.response.Symbol;
import com.bancobase.bootcamp.dto.response.SymbolsNameResponse;
import com.bancobase.bootcamp.http.APIExchangeRateClient;

@RestController
@Service
public class CurrenciesService {

    private final APIExchangeRateClient api;
    private List<CurrenciesDTO> currencies = new ArrayList<>();

    @Autowired
    public CurrenciesService(APIExchangeRateClient api) {
        this.api = api;
    }

    @Scheduled(fixedRate = 3600000)
    public void updateCurrencies() {
        ExchangeRateResponse exchangeRateResponse = api.getExchangeRate();
        SymbolsNameResponse symbolsNameResponse = api.getSymbolsName();

        Map<String, Double> exchangeRates = exchangeRateResponse.getRates();
        Map<String, Symbol> symbols = symbolsNameResponse.getSymbols();

        currencies = getCurrenciesFromSchema(exchangeRates, symbols);
    }

    public List<CurrenciesDTO> getCurrencies() {
        return currencies;
    }

    private List<CurrenciesDTO> getCurrenciesFromSchema(Map<String, Double> exchangeRates, Map<String, Symbol> symbols) {
        List<CurrenciesDTO> currencies = new ArrayList<>();
        for (Map.Entry<String, Double> entry : exchangeRates.entrySet()) {
            String symbol = entry.getKey();
            Double value = entry.getValue();
            currencies.add(CurrenciesDTO.getFromSchema(symbol, value, symbols));
        }
        return currencies;
    }

    @GetMapping("/currency")
    public List<CurrenciesDTO> getCurrencies2() {
        return this.getCurrencies();
    }
}

