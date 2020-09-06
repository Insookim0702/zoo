package com.zoo.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of="id")
@Entity
public class Account {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    // 이메일 인증이 된 계정인지 확인 플레그.
    private boolean emailVerified;

    //이메일 인증 시 사용할 토큰 값.
    private String emailCheckToken;

    //인증을 거치면 그 때, 가입이 된 걸로 즉, 가입날
    private LocalDateTime joinedAt;

    private LocalDateTime emailCheckTokenGeneratedAt;

    public void generateEmailCheckToken() {
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
        this.emailCheckToken = UUID.randomUUID().toString();
    }
}

