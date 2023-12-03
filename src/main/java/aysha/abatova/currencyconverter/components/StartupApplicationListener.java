package aysha.abatova.currencyconverter.components;

import aysha.abatova.currencyconverter.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;



import java.io.IOException;


@Component
public class StartupApplicationListener {

    @Autowired
    private CurrencyService currencyService;


    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws  IOException {
        currencyService.getRates();
    }
}
