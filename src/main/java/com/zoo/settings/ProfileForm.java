package com.zoo.settings;


import com.zoo.domain.Animal;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;
@Data
public class ProfileForm {
    @NotBlank
    @Length(max = 50)
    private String occupation;
    //private Set<Animal> favoriteAnimal;
}
