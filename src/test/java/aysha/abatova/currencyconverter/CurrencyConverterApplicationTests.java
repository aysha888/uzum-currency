package aysha.abatova.currencyconverter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import aysha.abatova.currencyconverter.controllers.ConversionController;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CurrencyConverterApplicationTests {


	@Autowired
	private ConversionController conversionController;



	@Test
	void contextLoads() {
		
		assertThat(conversionController).isNotNull();
		
	}

}
