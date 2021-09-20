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
    public String getPage(Model model) throws UserInDBNotFoundException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (userService.isBlocked(userService.loadUserByAuth(auth))) return "redirect:/block";


        List<User> users = userService.allUsers();

        model.addAttribute("userList",users);

        return "home";
    }


}
