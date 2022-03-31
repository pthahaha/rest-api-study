package kr.pe.acet.acetrestapi.accounts.repository;

import kr.pe.acet.acetrestapi.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String username);
}
