package com.bancobase.bootcamp.http;

import com.bancobase.bootcamp.dto.response.*;
import com.bancobase.bootcamp.exceptions.ServiceProviderException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class APIExchangeRateClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public ExchangeRateResponse getExchangeRate() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.exchangerate.host/latest")
                .queryParam("base", "MXN")
                .toUriString();

        HttpEntity<String> headersAndBody = new HttpEntity<>(headers);

        ResponseEntity<ExchangeRateResponse> responseEntity = this.restTemplate
                .exchange(url, HttpMethod.GET, headersAndBody, ExchangeRateResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        }

        throw ServiceProviderException
                .builder()
                .message("Oh no! An error occurred while connecting to our exchange rate provider.")
                .build();
    }

    public SymbolsNameResponse getSymbolsName() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.exchangerate.host/symbols")
                .toUriString();

        HttpEntity<String> headersAndBody = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<SymbolsNameResponse> responseEntity = restTemplate
                    .exchange(url, HttpMethod.GET, headersAndBody, SymbolsNameResponse.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                throw new RuntimeException("Oh no! An error occurred while connecting to our symbols provider.");
            }
        } catch (RestClientException ex) {
            throw new RuntimeException("Error while making the request to the symbols provider: " + ex.getMessage(),
                    ex);
        }
    }

}
