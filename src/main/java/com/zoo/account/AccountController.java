package com.zoo.account;

import com.zoo.domain.Account;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
public class AccountController {
    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpView(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }
        Account account = accountService.processSignUp(signUpForm);
        return "redirect:/";
    }

    @GetMapping("/recheck-email")
    public String checkEmail(@CurrentUser Account account, Model model) {
        if (!account.canSendConfirmEmail()) {
            model.addAttribute("oneHourError", "한 시간 후 다시 시도해 주세요.");
            model.addAttribute("lastRequestTime", account.getEmailCheckTokenGeneratedAt());
        }
        model.addAttribute("email", account.getEmail());
        return "account/recheck-email";
    }

    @GetMapping("/request-emailValidateToken")
    public String requestEmailValidateToken(@CurrentUser Account account, Model model) {
        accountService.emailAuthSend(account);
        model.addAttribute("message", "이메일이 발송되었습니다.");
        return "account/recheck-email";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String email, String token, Model model) {
        Account account = accountRepository.findByEmail(email);
        String resultPage = "account/checked-email";
        if (account == null) {
            model.addAttribute("error", "wrong.email");
            return resultPage;
        }
        if (!account.isValidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return resultPage;
        }
        accountService.completeSignUp(account);
        model.addAttribute("name", account.getName());
        model.addAttribute("numberOfUser", accountRepository.count());
        return resultPage;
    }
}
