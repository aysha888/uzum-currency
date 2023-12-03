package aysha.abatova.currencyconverter.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import aysha.abatova.currencyconverter.dtos.ConversionDto;
import aysha.abatova.currencyconverter.dtos.CurrencyRateExternalDto;
import aysha.abatova.currencyconverter.entities.Account;
import aysha.abatova.currencyconverter.entities.Currency;
import aysha.abatova.currencyconverter.exceptions.InvalidCurrencyCharCode;
import aysha.abatova.currencyconverter.exceptions.NotEnoughCurrency;
import aysha.abatova.currencyconverter.mappers.ExternalCurrencyMapper;
import aysha.abatova.currencyconverter.repositories.AccountRepository;
import aysha.abatova.currencyconverter.repositories.CurrencyRepository;











@Service
@Transactional
public class CurrencyService {
    @Value("${apiURL}")
    private String apiURL;

    private static final String UZS_CODE = "860";

    private static final String CBU_DATE_FORMAT = "dd.MM.yyyy";
    private static final BigDecimal COMMISSION_DIVIDER = new BigDecimal(10000); // i decided that i would store comission in 0.00 of percent, but in integer. so 25 means in fact 0.25% comission


    private final SimpleDateFormat format = new SimpleDateFormat(CBU_DATE_FORMAT);
    
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

    private BigDecimal comission(BigDecimal rate, int comission) {
        Boolean decreaseRate = comission < 0;
        comission = Math.abs(comission);
        BigDecimal normilizedComission = new BigDecimal(comission).divide(COMMISSION_DIVIDER, 20, RoundingMode.HALF_UP);  
        BigDecimal coef = new BigDecimal(1.000000).subtract(normilizedComission);
        if (decreaseRate) return rate.multiply(coef);
        
        return new BigDecimal(1.000000).divide(coef, 20, RoundingMode.HALF_UP).multiply(rate);
             
    }

    public void setComission(@Validated ComissionDto comissionDto) {
        Optional<Currency> optCurrency = currencyRepository.findByCharCode(comissionDto.getCharCode());
        if (!optCurrency.isPresent()) throw new InvalidCurrencyCharCode(comissionDto.getCharCode());
        Currency currency = optCurrency.get();
        currency.setComissionFrom(-comissionDto.getComissionFrom());
        currency.setComissionTo(comissionDto.getComissionTo());
        currencyRepository.save(currency);

    }


    public String convertDry(String fromCharCode, String toCharCode, BigDecimal amount) throws IOException {
        
        Optional<Currency> optCurrencyFrom = currencyRepository.findByCharCode(fromCharCode);
        Optional<Currency> optCurrencyTo = currencyRepository.findByCharCode(toCharCode);
        if (!optCurrencyFrom.isPresent()) throw new InvalidCurrencyCharCode(fromCharCode);
        if (!optCurrencyTo.isPresent()) throw new InvalidCurrencyCharCode(toCharCode);

        Currency currencyFrom = currencyRepository.findByCharCode(fromCharCode).get();
        Currency currencyTo = currencyRepository.findByCharCode(toCharCode).get();

        String currentDate = format.format(new Date());
        // TODO: on the weekends and holidays, all requests will be sent to central bank, because data will be different.
        // banks don't work on weekends and holidays, so api will always return us rates on last working day. Solution is to add calendar
        // easy solution is to check the day for weekends, but holidays will stay as edge case.
        if ((!currencyFrom.getDate().equals(currentDate) && !currencyFrom.getNumCode().equals(UZS_CODE)) || (!currencyTo.getDate().equals(currentDate) && !currencyTo.getNumCode().equals(UZS_CODE))) {
            getRates();
            currencyFrom = currencyRepository.findByCharCode(fromCharCode).get();
            currencyTo = currencyRepository.findByCharCode(toCharCode).get();
        }
        
        
        BigDecimal fromRate = currencyFrom.getNumCode().equals(UZS_CODE) ? currencyFrom.getRate() : comission(currencyFrom.getRate(), currencyFrom.getComissionFrom());
        BigDecimal toRate = currencyTo.getNumCode().equals(UZS_CODE) ? currencyTo.getRate() : comission(currencyTo.getRate(), currencyTo.getComissionTo());

        BigDecimal result = amount.multiply(fromRate).multiply(BigDecimal.valueOf(currencyFrom.getNominal()))
                            .divide(toRate, 6, RoundingMode.HALF_UP)
                            .divide(BigDecimal.valueOf(currencyTo.getNominal()), 4, RoundingMode.HALF_UP).setScale(6);


        return result.toString();
  
    }

    @Transactional
    public String convert(ConversionDto conversionDto) throws IOException {
       String result = convertDry(conversionDto.getFrom(), conversionDto.getTo(), conversionDto.getAmount());
       BigDecimal toWithdraw = new BigDecimal(result);
       // double request to db TODO: optimise
       Currency currencyFrom = currencyRepository.findByCharCode(conversionDto.getFrom()).get();
       Currency currencyTo = currencyRepository.findByCharCode(conversionDto.getTo()).get();
       Account accountFrom = accountRepository.findByCurrencyId(currencyFrom.getId()).get();
       Account accountTo = accountRepository.findByCurrencyId(currencyTo.getId()).get();
       int compared = accountTo.getBalance().compareTo(toWithdraw);
       if (compared < 0) throw new NotEnoughCurrency(currencyTo.getCharCode());
       accountFrom.setBalance(accountFrom.getBalance().add(conversionDto.getAmount()));
       accountTo.setBalance(accountTo.getBalance().subtract(toWithdraw));
       return result;

    }

 


}
