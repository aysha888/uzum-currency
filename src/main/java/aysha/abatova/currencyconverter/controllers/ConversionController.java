package aysha.abatova.currencyconverter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aysha.abatova.currencyconverter.dtos.ComissionDto;
import aysha.abatova.currencyconverter.dtos.CurrencyRateExternalDto;
import aysha.abatova.currencyconverter.presenters.RatesPresenter;
import aysha.abatova.currencyconverter.presenters.SuccessPresenter;
import aysha.abatova.currencyconverter.services.CurrencyService;

@RestController
public class ConversionController {

    @Autowired
    private CurrencyService currencyService;

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

}
