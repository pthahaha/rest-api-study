package kr.pe.acet.acetrestapi.configs;

import kr.pe.acet.acetrestapi.accounts.Account;
import kr.pe.acet.acetrestapi.accounts.AccountRole;
import kr.pe.acet.acetrestapi.accounts.service.AccountService;
import kr.pe.acet.acetrestapi.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Set;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @DisplayName("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception{
        // Given
        String username = "sshaple@daum.net";
        String password = "park-taeha";
        Account taeha = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(taeha);

        String clientId="myApp";
        String clientSecret="pass";
        this.mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(clientId, clientSecret)) // header
                        .param("username", username)
                        .param("password", password)
                        .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }
}