package com.zoo.account;

import com.zoo.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@RequiredArgsConstructor
@Component
public class SignUpFormValidator implements Validator {
    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SignUpForm signUpForm = (SignUpForm)object;
        if(!signUpForm.getCheckPassword().equals(signUpForm.getPassword())){
            errors.rejectValue("password","invalid.password", new Object[]{signUpForm.getPassword()}, "비밀번호가 서로 다릅니다.");
        }
        if(accountRepository.existsByEmail(signUpForm.getEmail())){
            errors.rejectValue("email","invalid.email", new Object[]{signUpForm.getEmail()},"이미 사용 중인 이메일입니다.");
        }
    }
}
