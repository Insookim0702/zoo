package com.zoo.settings;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class PasswordForm {
    @NotBlank
    @Length(min = 8,max = 30)
    private String checkNewPassword;

    @NotBlank
    @Length(min = 8,max = 20)
    private String newPassword;
}
