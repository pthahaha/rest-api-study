package kr.pe.acet.acetrestapi.accounts.service;

import kr.pe.acet.acetrestapi.accounts.Account;
import kr.pe.acet.acetrestapi.accounts.AccountRole;
import kr.pe.acet.acetrestapi.accounts.repository.AccountRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void findByUsername(){
        // Given
        String password = "taeha";
        String username = "sshaple@naver.com";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountRepository.save(account);


        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        // Then
        assertThat(userDetails.getPassword().equals(password));

    }

    @Test
    public void findByUsernameFail(){
        Assertions.assertThrows(UsernameNotFoundException.class, ()->{
            String username = "abc@gmail.com";
            accountService.loadUserByUsername(username);
        }, "예외가 발생하지 않았습니다.");

    }

}