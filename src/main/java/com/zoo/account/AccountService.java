package com.zoo.account;

import com.zoo.domain.Account;
import com.zoo.settings.PasswordForm;
import com.zoo.settings.ProfileForm;
import com.zoo.settings.form.NotificationsForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
@Transactional
@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JavaMailSender javaMailSender;
    public Account processSignUp(@Valid SignUpForm signUpForm) {
        //디비에 저장
        Account account = saveAccountTODB(signUpForm);
        emailAuthSend(account);
        login(account);
        return account;
    }
    public void emailAuthSend(Account account){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("동물원증 예약 시스템, 회원가입 이메일 인증");
        simpleMailMessage.setTo(account.getEmail());
        account.getEmailCheckTokenGeneratedAt(); //이걸 해야, 이메일 재인증 요청을 하면, 토큰 생성 시간이 최신으로 변경됨.
        accountRepository.save(account);
        simpleMailMessage.setText("/check-email-token?token="+account.getEmailCheckToken()+"&email="+account.getEmail());
        javaMailSender.send(simpleMailMessage);
    }

    public Account saveAccountTODB(@Valid SignUpForm signUpForm) {
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm, Account.class);
        account.generateEmailCheckToken();
        return accountRepository.save(account);
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(token);
    }

    public void completeSignUp(Account account) {
        account.completeSignUp();
        login(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        if(account == null){
            throw new UsernameNotFoundException(email);
        }
        return new UserAccount(account);
    }

    public void updateProfile(Account account, ProfileForm profileForm) {
        modelMapper.map(profileForm, account);
        accountRepository.save(account);
    }

    public void updatePassword(Account account, PasswordForm passwordForm) {
        account.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
        accountRepository.save(account);
    }

    public void updateAlarm(Account account, NotificationsForm notificationsForm) {
        //Account persistentAccount = accountRepository.findByEmail(account.getEmail());
        //persistentAccount.setEventAlarmByEmail(notificationsForm.isEventAlarmByEmail());
        //persistentAccount.setEventAlarmByWeb(notificationsForm.isEventAlarmByWeb());
        modelMapper.map(notificationsForm, account);
        accountRepository.save(account);
    }
}
