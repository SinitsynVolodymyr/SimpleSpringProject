package com.controller;

import com.entity.SocialNetwork;
import com.entity.User;
import com.service.SocialNetworkService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("home")
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    SocialNetworkService socialNetworkService;

    private String messageFormat = "На сайте заригистрировалось %s человек.";
    private String messageSocFormat = "Из них в %s: %d человек.";

    @GetMapping
    public String getPage(Model model){

        List<User> users = userService.allUsers();
        List<String> socMessages = getSocMessages(users);
        model.addAttribute("socCountList",socMessages);

        model.addAttribute("amountUsers", String.format(messageFormat,users.size()));
        return "home";
    }

    private List<String> getSocMessages(List<User> users) {
        List<String> socCountMessages = new ArrayList<>();
        socialNetworkService.allSocialNetwork().stream().forEach((soc)->{
            long count = users.stream().filter(user -> user.getSocialNetwork().equals(soc)).count();
            String message = String.format(messageSocFormat, soc.getName(), count);
            socCountMessages.add(message);
        });
        return socCountMessages;
    }


}
