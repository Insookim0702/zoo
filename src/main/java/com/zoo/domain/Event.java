package com.zoo.domain;

import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of ="id")
@Entity
public class Event {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String eventName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long price;

    private String content;

    private Long participantNumber;

    private String eventSpot;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String eventImage;
}
