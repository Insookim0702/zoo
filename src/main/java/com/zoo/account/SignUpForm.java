package com.zoo.account;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Data
public class SignUpForm {
    @NotBlank
    @Length(min = 2, max = 20)
    @Pattern(regexp = "^[가-힣]{3,10}$")
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min=8,max = 20)
    private String checkPassword;

    @NotBlank
    @Length(min=8,max = 20)
    private String password;
}
