package ru.tbank.mock.dao.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.mock.dao.entity.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {

  List<Account> findByCurrencyCode(String currencyCode);
}
