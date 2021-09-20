package com.controller;

import com.entity.SocialNetwork;
import com.entity.User;
import com.exception.UserInDBNotFoundException;
import com.service.SocialNetworkService;
import com.service.UserService;
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
@RequestMapping("home")
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    SocialNetworkService socialNetworkService;

    @GetMapping
    public String getPage(Model model)  {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String thisUsername="";
        try {
            User user = userService.loadUserByAuth(auth);
            thisUsername = user.getUsername();
            if (userService.isBlocked(user)) return "redirect:/block";
        } catch (UserInDBNotFoundException e) {
            auth.setAuthenticated(false);
            return "redirect:/";
        }


        List<User> users = userService.allUsers();

        model.addAttribute("userList",users);
        model.addAttribute("username",thisUsername);

        return "home";
    }


}
