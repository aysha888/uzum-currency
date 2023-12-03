package aysha.abatova.currencyconverter.repositories;


import aysha.abatova.currencyconverter.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
    Optional<Currency> findByCharCode(String charCode);
}
