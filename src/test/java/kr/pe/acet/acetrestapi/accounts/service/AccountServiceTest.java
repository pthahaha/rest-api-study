package kr.pe.acet.acetrestapi.accounts.service;

import kr.pe.acet.acetrestapi.accounts.Account;
import kr.pe.acet.acetrestapi.accounts.AccountRole;
import kr.pe.acet.acetrestapi.common.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

class AccountServiceTest extends BaseTest {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername(){
        // Given
        String password = "taehaha";
        String username = "sshaple@email.com";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
//        this.accountRepository.save(account);
        this.accountService.saveAccount(account); // encoder 추가

        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();

    }

    @Test
    public void findByUsernameFail(){
        Assertions.assertThrows(UsernameNotFoundException.class, ()->{
            String username = "abc@gmail.com";
            accountService.loadUserByUsername(username);
        }, "예외가 발생하지 않았습니다.");

    }
}