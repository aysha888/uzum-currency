package aysha.abatova.currencyconverter.controllers;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import aysha.abatova.currencyconverter.dtos.ConversionDto;
import aysha.abatova.currencyconverter.dtos.ComissionDto;
import aysha.abatova.currencyconverter.dtos.CurrencyRateExternalDto;
import aysha.abatova.currencyconverter.exceptions.InvalidCurrencyCharCode;
import aysha.abatova.currencyconverter.exceptions.NotEnoughCurrency;
import aysha.abatova.currencyconverter.presenters.ConversionPresenter;
import aysha.abatova.currencyconverter.presenters.ErrorPresenter;
import aysha.abatova.currencyconverter.presenters.RatesPresenter;
import aysha.abatova.currencyconverter.presenters.SuccessPresenter;
import aysha.abatova.currencyconverter.services.CurrencyService;

@RestController
public class ConversionController {

    @Autowired
    private CurrencyService currencyService;


    @ExceptionHandler(InvalidCurrencyCharCode.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // should be bad request but requirements... well bad
    public ErrorPresenter handleNotFoundError(InvalidCurrencyCharCode ex) {
        return new ErrorPresenter(ex.getErrorCode(), ex.getMessage(), ex.getCharCode());
    }

    @ExceptionHandler(NotEnoughCurrency.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // should be bad request but requirements... well bad
    public ErrorPresenter handleNotEnoughError(NotEnoughCurrency ex) {
        return new ErrorPresenter(ex.getErrorCode(), ex.getMessage(), ex.getCharCode());
    }

    @ModelAttribute("comission")
    public ComissionDto comissionDto() {
        return new ComissionDto();
    }

    @ModelAttribute("conversion")
    public ConversionDto conversionDto() {
        return new ConversionDto();
    }

    @GetMapping("/officialrates")
    public RatesPresenter rates(@RequestParam(name = "date") String date,
            @RequestParam(name = "currency", defaultValue = "USD") String charCode) {
        CurrencyRateExternalDto rate = currencyService.getRate(date, charCode);
        String rateResult = rate.getRate();
        return new RatesPresenter(rateResult);
    }

    @PostMapping("/comission")
    public SuccessPresenter setComission(@RequestBody ComissionDto comissionDto) {
        currencyService.setComission(comissionDto);
        return new SuccessPresenter();
    }

    @GetMapping("/convert")
    public ConversionPresenter convertDry(@RequestParam(name = "from") String fromCharCode, @RequestParam(name = "to") String toCharCode, @RequestParam(name = "amount") BigDecimal amount) throws IOException {
        String result = currencyService.convertDry(fromCharCode, toCharCode, amount);
        return new ConversionPresenter(result);
    }

    @PostMapping("/convert")
    public ConversionPresenter convert(@RequestBody ConversionDto conversionDto) throws IOException {
        String conversionResult =  currencyService.convert(conversionDto);
        return new ConversionPresenter(conversionResult);
       
    }

}
