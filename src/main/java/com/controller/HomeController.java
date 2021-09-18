package com.controller;

import com.entity.SocialNetwork;
import com.entity.User;
import com.exception.SocialNetworkNotFoundException;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Random;
import java.util.UUID;

@Controller
public class HomeController {
    @Autowired
    UserService userService;

    private int countUser = 14;

    private String messageFormat = "На сайте заригистрировалось %s человек.";

    @GetMapping
    public String unlogHome(Model model){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User("Biba"+new Random().nextInt(100000));
        SocialNetwork facebook = new SocialNetwork("facebook");
        user.setSocialNetwork(facebook);
        user.setSocIdentifier(UUID.randomUUID().toString());
        try {
            userService.saveUser(user);
        } catch (SocialNetworkNotFoundException e) {
            return "Соціальна мережа не знадена";
        }
        model.addAttribute("amountUsers", String.format(messageFormat,userService.allUsers().size()));
        return "unlogHome";
    }


}
