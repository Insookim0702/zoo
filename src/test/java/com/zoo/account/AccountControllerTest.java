package com.zoo.account;

import com.zoo.domain.Account;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @AfterEach
    void after(){
        accountRepository.deleteAll();
    }

    @DisplayName("계정만들기 뷰 페이지 접근 테스트")
    @Test
    void signUpView() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(unauthenticated());
    }

    @DisplayName("계정 만들기 submit")
    @Test
    void signUpSubmit() throws Exception {
        mockMvc.perform(post(    "/sign-up")
                .param("name","동물원")
                .param("checkPassword", "123123123")
                .param("password","123123123")
                .param("email","동물원@email.com")
        .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
        .andExpect(authenticated());
        Account account = accountRepository.findByEmail("동물원@email.com");
        assertNotNull(account);
        assertFalse(account.getPassword().equals("123123123"));
        assertFalse(account.isEmailVerified());
    }

    @DisplayName("[실패]계정 만들기-이메일 중복 submit")
    @Test
    void duplicationEmailSubmit() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setName("동물원");
        signUpForm.setEmail("동물원@email.com");
        signUpForm.setPassword("123123123");
        signUpForm.setCheckPassword("123123123");
        accountService.processSignUp(signUpForm);
        Account account = accountRepository.findByEmail("동물원@email.com");
        assertNotNull(account);
        assertFalse(account.isEmailVerified());
        mockMvc.perform(post(    "/sign-up")
                .param("name","동물")
                .param("checkPassword", "123123123")
                .param("password","123123123")
                .param("email","동물원@email.com")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
        Account acc = accountRepository.findByName("동물");
        assertNull(acc);
    }

    @DisplayName("[성공]이메일 인증 재요청 페이지 뷰")
    @Test
    void recheckEmailPage() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setPassword("123123123");
        signUpForm.setEmail("동물원@email.com");
        signUpForm.setName("동물원");
        Account account = accountService.processSignUp(signUpForm);
        Account byEmail = accountRepository.findByEmail(account.getEmail());
        assertNotNull(byEmail);

        mockMvc.perform(get("/recheck-email"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("email"))
                .andExpect(view().name("account/recheck-email"))
                .andExpect(authenticated());
    }

    @DisplayName("[성공]이메일 재인증 요청")
    @Test
    void requestEmailCheck() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setPassword("123123123");
        signUpForm.setEmail("동물원@email.com");
        signUpForm.setName("동물원");
        Account account = accountService.processSignUp(signUpForm);
        Account byEmail = accountRepository.findByEmail(account.getEmail());
        assertNotNull(byEmail.getEmailCheckTokenGeneratedAt());

        mockMvc.perform(get("/request-emailValidateToken"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("oneHourError"))
                .andExpect(view().name("account/recheck-email"));
    }

    @DisplayName("[성공]이메일 인증")
    @Test
    void checkEmail() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setPassword("123123123");
        signUpForm.setEmail("동물원@email.com");
        signUpForm.setName("동물원");
        Account account = accountService.processSignUp(signUpForm);
        Account byEmail = accountRepository.findByEmail(account.getEmail());
        assertNotNull(byEmail);

        mockMvc.perform(get("/check-email-token")
        .param("email","동물원@email.com")
        .param("token",byEmail.getEmailCheckToken()))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(view().name("account/checked-email"));
        Account validedAccount = accountRepository.findByEmail("동물원@email.com");
        assertNotNull(validedAccount.getEmailCheckTokenGeneratedAt());
        assertTrue(validedAccount.isEmailVerified());
    }

    @DisplayName("[실패]이메일 인증")
    @Test
    void failCheckEmailToken() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setPassword("123123123");
        signUpForm.setEmail("동물원@email.com");
        signUpForm.setName("동물원");
        Account account = accountService.processSignUp(signUpForm);
        Account byEmail = accountRepository.findByEmail(account.getEmail());
        assertNotNull(byEmail);

        mockMvc.perform(get("/check-email-token")
                .param("email","동물")
                .param("token",byEmail.getEmailCheckToken()))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"));
        Account validedAccount = accountRepository.findByEmail("동물원@email.com");
        assertFalse(validedAccount.isEmailVerified());
    }

}