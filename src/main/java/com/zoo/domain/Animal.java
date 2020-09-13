package com.zoo.domain;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.websocket.server.ServerEndpoint;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter @EqualsAndHashCode(of="id")
@Entity
public class Animal {
    @Id @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
}
