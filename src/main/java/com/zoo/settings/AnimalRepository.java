package com.zoo.settings;

import com.zoo.domain.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Animal findByName(String animalName);
}
