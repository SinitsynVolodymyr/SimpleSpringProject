package com.controller;

import com.entity.User;
import com.service.SocialNetworkService;
import com.service.UserService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    UserService userService;
    @Autowired
    SocialNetworkService socialNetworkService;

    private String messageFormat = "На сайте заригистрировалось %s человек.";
    private String messageSocFormat = "Из них в %s: %d человек.";

    @GetMapping
    public String getPage(Model model){
        String isAuthRed = redirect();
        if (isAuthRed != null) return isAuthRed;
        List<User> users = userService.allUsers();
        List<String> socMessages = getSocMessages(users);
        model.addAttribute("socCountList",socMessages);
        model.addAttribute("amountUsers", String.format(messageFormat,users.size()));
        return "index";
    }

    @Nullable
    private String redirect() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_USER")){
            return "redirect:/home";
        }
        return null;
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
