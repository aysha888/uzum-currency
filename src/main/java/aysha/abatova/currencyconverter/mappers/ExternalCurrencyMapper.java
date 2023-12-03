package aysha.abatova.currencyconverter.mappers;

import java.math.BigDecimal;


import org.springframework.stereotype.Component;

import aysha.abatova.currencyconverter.dtos.CurrencyRateExternalDto;
import aysha.abatova.currencyconverter.entities.Account;
import aysha.abatova.currencyconverter.entities.Currency;


@Component
public class ExternalCurrencyMapper {

    
    private static BigDecimal startAccountValue = new BigDecimal("1000000000000000");

    public static Currency currencyMapper(CurrencyRateExternalDto externalCurrency) {
        Currency currency = new Currency();
        currency.setCurrency_name(externalCurrency.getCcyNm_EN());
        currency.setCharCode(externalCurrency.getCcy());
        currency.setNominal(Integer.parseInt(externalCurrency.getNominal()));
        currency.setNumCode(externalCurrency.getCode());
        currency.setDate(externalCurrency.getDate());
        currency.setRate(new BigDecimal(externalCurrency.getRate()));
        return currency;
    }


    public static Account mapToAccount(Currency currency) {
        Account account = new Account();
        account.setBalance(startAccountValue);
        account.setCurrency(currency);
        return account;
    }


}
