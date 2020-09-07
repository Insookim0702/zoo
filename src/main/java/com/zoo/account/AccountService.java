package com.zoo.account;

import com.zoo.domain.Account;
import javassist.Loader;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JavaMailSender javaMailSender;
    public void processSignUp(@Valid SignUpForm signUpForm) {
        //디비에 저장
        Account account = saveAccountTODB(signUpForm);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("동물원증 예약 시스템, 회원가입 이메일 인증");
        simpleMailMessage.setTo(account.getEmail());
        simpleMailMessage.setText("/check-email-token?token="+account.getEmailCheckToken()+"&email="+account.getEmail());
        javaMailSender.send(simpleMailMessage);
        //이메일 인증 토큰 전송
        //로그인 처리
    }

    private Account saveAccountTODB(@Valid SignUpForm signUpForm) {
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm, Account.class);
        account.generateEmailCheckToken();
        return accountRepository.save(account);
    }
}
