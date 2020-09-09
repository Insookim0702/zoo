package com.zoo.main;

import com.zoo.account.AccountRepository;
import com.zoo.account.AccountService;
import com.zoo.account.SignUpForm;
import com.zoo.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    @BeforeEach
    void before(){
        //given
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setName("동물원");
        signUpForm.setEmail("동물원@email.com");
        signUpForm.setPassword("123123123");
        signUpForm.setCheckPassword("123123123");
        accountService.processSignUp(signUpForm);
        Account account = accountRepository.findByEmail("동물원@email.com");
        assertNotNull(account);
        assertFalse(account.isEmailVerified());
    }
    @AfterEach
    void after(){
        accountRepository.deleteAll();
    }

    @DisplayName("로그인 ")
    @Test
    void loginTest() throws Exception {

        //then
        mockMvc.perform(post("/login")
                .with(csrf())
                .param("username", "동물원@email.com")
                .param("password","123123123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("동물원"));
    }

    @DisplayName("로그인 실패")
    @Test
    void loginFail() throws Exception {
        mockMvc.perform(post("/login")
                .param("username","동물@email.com")
                .param("password","123123123")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/logout").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }

}