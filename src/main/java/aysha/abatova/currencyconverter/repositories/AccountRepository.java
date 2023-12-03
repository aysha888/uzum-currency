package aysha.abatova.currencyconverter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aysha.abatova.currencyconverter.entities.Account;


import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCurrencyId(Integer id);
}

