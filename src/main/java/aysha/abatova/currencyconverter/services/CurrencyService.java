package aysha.abatova.currencyconverter.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import aysha.abatova.currencyconverter.dtos.ComissionDto;
import aysha.abatova.currencyconverter.dtos.CurrencyRateExternalDto;
import aysha.abatova.currencyconverter.entities.Account;
import aysha.abatova.currencyconverter.entities.Currency;
import aysha.abatova.currencyconverter.exceptions.InvalidCurrencyCharCode;
import aysha.abatova.currencyconverter.mappers.ExternalCurrencyMapper;
import aysha.abatova.currencyconverter.repositories.AccountRepository;
import aysha.abatova.currencyconverter.repositories.CurrencyRepository;











@Service
@Transactional
public class CurrencyService {
    @Value("${apiURL}")
    private String apiURL;
    
    @Autowired
    private RestTemplate restTemplate;

   @Autowired
   private CurrencyRepository currencyRepository;


   @Autowired
   private AccountRepository accountRepository;
    


    public void getRates() throws IOException {
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

    private void populateAccountsIfNotPresent(List<Currency> currencies) {
        List<Account> accounts = accountRepository.findAll();
        Boolean alreadyHaveAccounts = accounts.size() > 1; // because we have one after migration it's uzs
        if (alreadyHaveAccounts) return;
        accounts = currencies.stream().map(ExternalCurrencyMapper::mapToAccount).collect(Collectors.toList());
        accountRepository.saveAll(accounts);
    }

    
   
    // here i use simple implementation. Because we have currencies after start of application, 
    // there will never be situation where app will try to insert two same currencies. Except if it's a new currency and two users use this method at the same.
    // Which i deem highly unlikely.
    private Currency upsertCurrency(Currency currency) {
        Optional<Currency> existing = currencyRepository.findByCharCode(currency.getCharCode());
        if (!existing.isPresent()) return currencyRepository.save(currency);
        existing.get().setDate(currency.getDate());
        existing.get().setRate(currency.getRate());
        return currencyRepository.save(existing.get());
    }

    public CurrencyRateExternalDto getRate(String date, String charCode) {
        String url = createStringURLForRate(date, charCode);
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CurrencyRateExternalDto>>() {}).getBody().get(0);
        
    }

    private String createStringURLForRate(String date, String charCode) {
        StringBuilder builder = new StringBuilder();
        return builder.append(apiURL).append("/").append(charCode.toUpperCase()).append("/").append(date).toString();

    }

    public void setComission(@Validated ComissionDto comissionDto) {
        Optional<Currency> optCurrency = currencyRepository.findByCharCode(comissionDto.getCharCode());
        if (!optCurrency.isPresent()) throw new InvalidCurrencyCharCode(comissionDto.getCharCode());
        Currency currency = optCurrency.get();
        currency.setComissionFrom(-comissionDto.getComissionFrom());
        currency.setComissionTo(comissionDto.getComissionTo());
        currencyRepository.save(currency);

    }

 


}
