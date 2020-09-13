package com.zoo.settings;

import com.zoo.WithAccount;
import com.zoo.account.AccountRepository;
import com.zoo.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class SettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;
    @AfterEach
    void afterEach(){
        accountRepository.deleteAll();
    }

    @DisplayName("프로필 설정 뷰")
    @WithAccount("동물원@email.com")
    @Test
    void 뷰테스트_프로필설정() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profileForm"))
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE))
                .andExpect(authenticated());
    }
    @DisplayName("비밀번호 설정 뷰")
    @WithAccount("동물원@email.com")
    @Test
    void 뷰테스트_비밀번호설정() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(view().name(SettingsController.SETTINGS_PASSWORD))
                .andExpect(authenticated());
    }

    @DisplayName("알람 설정 뷰")
    @WithAccount("동물원@email.com")
    @Test
    void 뷰테스트_알람설정() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_ALARM))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("notificationsForm"))
                .andExpect(view().name(SettingsController.SETTINGS_ALARM))
                .andExpect(authenticated());
    }

    @DisplayName("동물 설정 뷰")
    @WithAccount("동물원@email.com")
    @Test
    void 뷰테스트_동물_설정() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_FAVORITE_ANIMAL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(view().name(SettingsController.SETTINGS_FAVORITE_ANIMAL))
                .andExpect(authenticated());
    }

    @DisplayName("계정 설정 뷰")
    @WithAccount("동물원@email.com")
    @Test
    void 뷰테스트_계정설정() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_ACCOUNT))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(view().name(SettingsController.SETTINGS_ACCOUNT))
                .andExpect(authenticated());
    }

    @DisplayName("프로필설정변경요청")
    @WithAccount("동물원@email.com")
    @Test
    void 프로필설정변경요청() throws Exception {
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE)
        .param("occupation","개발자")
        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PROFILE));
    }

    @DisplayName("비밀변호변경요청")
    @WithAccount("동물원@email.com")
    @Test
    void 비밀변호변경요청() throws Exception {
        mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD)
                .param("newPassword","rkskekfk")
                .param("checkNewPassword","rkskekfk")
        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(model().attributeDoesNotExist("errors"))
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PASSWORD));
    }

    @DisplayName("비밀호불일치")
    @WithAccount("동물원@email.com")
    @Test
    void 비밀번호불일치() throws Exception {
        mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD)
                .param("newPassword","rksk")
                .param("checkNewPassword","rkskekfk123")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"))
                .andExpect(model().hasErrors())
                .andExpect(view().name(SettingsController.SETTINGS_PASSWORD));
    }

    @DisplayName("알람설정변경요청")
    @WithAccount("동물원@email.com")
    @Test
    void 알람설정변경요청() throws Exception {
        Account oldAccount = accountRepository.findByEmail("동물원@email.com");
        assertFalse(oldAccount.isEventAlarmByEmail());
        assertFalse(oldAccount.isEventAlarmByWeb());
        mockMvc.perform(post(SettingsController.SETTINGS_ALARM)
                .param("eventAlarmByWeb", String.valueOf(true))
                .param("eventAlarmByEmail", String.valueOf(true))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl(SettingsController.SETTINGS_ALARM));
        Account updateAccount = accountRepository.findByEmail("동물원@email.com");
        assertTrue(updateAccount.isEventAlarmByEmail());
        assertTrue(updateAccount.isEventAlarmByWeb());
    }
}