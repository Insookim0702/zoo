package com.zoo.manage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/manage")
@Controller
public class ManageController {
    @GetMapping("/zooAnimal")
    public String getZooAnimalView(){
        return "manage/animal";
    }

}
