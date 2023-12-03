package aysha.abatova.currencyconverter.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import aysha.abatova.currencyconverter.dtos.CurrencyRateExternalDto;
import aysha.abatova.currencyconverter.entities.Currency;











@Service
@Transactional
public class CurrencyService {
    @Value("${apiURL}")
    private String apiURL;
    
    @Autowired
    private RestTemplate restTemplate;

   
    


    public void getRates() throws Exception {
        ResponseEntity<List<CurrencyRateExternalDto>> response = restTemplate.exchange(
            apiURL,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<CurrencyRateExternalDto>>() {});
        List<Currency> currencies = saveCurrencies(response.getBody());
        populateAccountsIfNotPresent(currencies);

    }

    private List<Currency> saveCurrencies(List<CurrencyRateExternalDto> fullCurrencies) {

        List<Currency> currencies = fullCurrencies.stream().map(ExternalCurrencyMapper::currencyMapper).collect(Collectors.toList());
        return currencies.stream().map(this::upsertCurrency).toList();
    }

    
   
 
}
