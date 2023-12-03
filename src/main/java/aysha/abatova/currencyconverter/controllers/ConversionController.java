package aysha.abatova.currencyconverter.controllers;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aysha.abatova.currencyconverter.dtos.ConversionDto;
import aysha.abatova.currencyconverter.dtos.ComissionDto;
import aysha.abatova.currencyconverter.dtos.CurrencyRateExternalDto;
import aysha.abatova.currencyconverter.presenters.ConversionPresenter;
import aysha.abatova.currencyconverter.presenters.RatesPresenter;
import aysha.abatova.currencyconverter.presenters.SuccessPresenter;
import aysha.abatova.currencyconverter.services.CurrencyService;

@RestController
public class ConversionController {

    @Autowired
    private CurrencyService currencyService;


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
