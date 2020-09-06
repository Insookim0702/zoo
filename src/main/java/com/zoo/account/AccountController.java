package com.zoo.account;

import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AccountController {
    @GetMapping("/sign-up")
    public String signUpView(Model model){
        model.addAttribute("signUpForm", new SignUpForm());
        return "/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid SignUpForm signUpForm, Model model, Errors errors){
        //AccountService.processSignUp(signUpForm);
        return "redirect:/";
    }
}
