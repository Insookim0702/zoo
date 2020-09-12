package com.zoo;

import com.zoo.account.AccountService;
import com.zoo.account.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {
    private final AccountService accountService;
    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {
        String email = withAccount.value();
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setName("동물원");
        signUpForm.setEmail("동물원@email.com");
        signUpForm.setPassword("123123123");
        signUpForm.setCheckPassword("123123123");
        accountService.saveAccountTODB(signUpForm);

        UserDetails principal = accountService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(token);
        return securityContext;
    }
}
