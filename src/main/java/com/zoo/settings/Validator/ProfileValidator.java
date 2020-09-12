package com.zoo.settings.Validator;

import com.zoo.settings.ProfileForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProfileValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(ProfileForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}
