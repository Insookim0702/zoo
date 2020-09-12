package com.zoo.settings.Validator;

import com.zoo.settings.PasswordForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PasswordValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(PasswordForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        PasswordForm passwordForm = (PasswordForm)object;
        if(!passwordForm.getCheckNewPassword().equals(passwordForm.getNewPassword())) {
            errors.rejectValue("checkNewPassword","invalid.checkNewPassword", new Object[]{passwordForm.getCheckNewPassword()}, "비밀번호가 다릅니다.");
        }
    }
}
