package aysha.abatova.currencyconverter.services;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import aysha.abatova.currencyconverter.dtos.ConversionDto;

import aysha.abatova.currencyconverter.entities.Account;
import aysha.abatova.currencyconverter.entities.Currency;
import aysha.abatova.currencyconverter.exceptions.NotEnoughCurrency;
import aysha.abatova.currencyconverter.repositories.AccountRepository;
import aysha.abatova.currencyconverter.repositories.CurrencyRepository;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    @Transactional // Ensure the test method is within a transaction
    public void testConvert() throws IOException {
        // Mock data
        ConversionDto conversionDto = new ConversionDto();
        conversionDto.setAmount(new BigDecimal(100));
        conversionDto.setFrom("USD");
        conversionDto.setTo("UZS");
        Currency currencyFrom = new Currency();
        currencyFrom.setCharCode("USD");
        currencyFrom.setComissionFrom(-100);
        currencyFrom.setComissionTo(200);
        currencyFrom.setDate("03.12.2023");
        currencyFrom.setNominal(1);
        currencyFrom.setNumCode("840");
        currencyFrom.setRate(new BigDecimal(11000));
        currencyFrom.setId(1);
        Currency currencyTo = new Currency();
        currencyTo.setCharCode("UZS");
        currencyTo.setComissionFrom(0);
        currencyTo.setComissionTo(0);
        currencyTo.setDate("03.12.2023");
        currencyTo.setNominal(1);
        currencyTo.setNumCode("860");
        currencyTo.setRate(new BigDecimal(1));
        currencyTo.setId(2);
        Account accountFrom = new Account();
        accountFrom.setBalance(new BigDecimal(1000000000));
        accountFrom.setCurrency(currencyFrom);
        currencyFrom.setAccount(accountFrom);
        Account accountTo = new Account();
        accountTo.setBalance(new BigDecimal(1000000000));
        accountTo.setCurrency(currencyTo);
        currencyTo.setAccount(accountTo);

        when(currencyRepository.findByCharCode("USD")).thenReturn(Optional.of(currencyFrom));
        when(currencyRepository.findByCharCode("UZS")).thenReturn(Optional.of(currencyTo));
        when(accountRepository.findByCurrencyId(currencyFrom.getId())).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findByCurrencyId(currencyTo.getId())).thenReturn(Optional.of(accountTo));

        try {

            String result = currencyService.convert(conversionDto);
            // if i'm right rate is 11 000 then 100 usd is 1 100 000 uzs and -1% comission
            // should be 1 089 000
            assertEquals("1089000.000000", result);

            assertEquals(new BigDecimal("1000000100"), accountFrom.getBalance());
            assertEquals(new BigDecimal("998911000.000000"), accountTo.getBalance());
        } catch (NotEnoughCurrency e) {
            fail("NotEnoughCurrency exception not expected.");
        }
    }

    @Test
    @Transactional
    public void testConvertFromUSDToRUB() throws IOException {
        // Mock data for USD to RUB conversion
        ConversionDto conversionDto = new ConversionDto();
        conversionDto.setAmount(new BigDecimal(100));
        conversionDto.setFrom("USD");
        conversionDto.setTo("RUB");

        Currency currencyFrom = new Currency();
        currencyFrom.setCharCode("USD");
        currencyFrom.setRate(new BigDecimal(11000));
        currencyFrom.setComissionFrom(-100);
        currencyFrom.setComissionTo(200);
        currencyFrom.setDate("03.12.2023");
        currencyFrom.setNominal(1);
        currencyFrom.setNumCode("840");
        
        currencyFrom.setId(1);
        

        Currency currencyTo = new Currency();
        currencyTo.setCharCode("RUB");
        currencyTo.setRate(new BigDecimal(110));
        currencyTo.setComissionFrom(-100);
        currencyTo.setComissionTo(200);
        currencyTo.setDate("03.12.2023");
        currencyTo.setNominal(1);
        currencyTo.setNumCode("666");
        currencyTo.setId(2);


        Account accountFrom = new Account();
        accountFrom.setBalance(new BigDecimal(1000000000));
        accountFrom.setCurrency(currencyFrom);
        currencyFrom.setAccount(accountFrom);

        Account accountTo = new Account();
        accountTo.setBalance(new BigDecimal(1000000000));
        accountTo.setCurrency(currencyTo);
        currencyTo.setAccount(accountTo);

     



        when(currencyRepository.findByCharCode("USD")).thenReturn(Optional.of(currencyFrom));
        when(currencyRepository.findByCharCode("RUB")).thenReturn(Optional.of(currencyTo));
        when(accountRepository.findByCurrencyId(currencyFrom.getId())).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findByCurrencyId(currencyTo.getId())).thenReturn(Optional.of(accountTo));      

        try {
            String result = currencyService.convert(conversionDto);
            // if I'm right, the rate is 110, so 100 USD should be 9702 RUB
            assertEquals("9702.000000", result);

            assertEquals(new BigDecimal("1000000100"), accountFrom.getBalance());
            assertEquals(new BigDecimal("999990298.000000"), accountTo.getBalance());
        } catch (NotEnoughCurrency e) {
            fail("NotEnoughCurrency exception not expected.");
        }
    }

}
