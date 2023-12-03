package aysha.abatova.currencyconverter.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;











@Service
@Transactional
public class CurrencyService {
    @Value("${apiURL}")
    private String apiURL;
    

   
    


    public void getRates() throws Exception {
        throw new Exception("Not Implemented");

    }

    
   
 
}
