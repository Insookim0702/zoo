package com.zoo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Entity
public class ZooAnimal {
    @Id @GeneratedValue
    private Long id;
    private String korName;
    private String engName;
    private String animalClassification;
    private String eating;
    private LocalDateTime registrationDate;
    @Lob
    private String img;
    private String introduce;
}
