package com.controller;

import com.entity.Status;
import com.entity.User;
import com.exception.SocialNetworkNotFoundException;
import com.repo.StatusRepository;
import com.service.SocialNetworkService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("manage")
public class UserManageController {
    @Autowired
    UserService userService;
    @Autowired
    StatusRepository statusRepository;

    @PostMapping
    public String block(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        user.setStatus(statusRepository.findByName(Status.BLOCK.getName()));
        try {
            userService.updateUser(user);
        } catch (SocialNetworkNotFoundException e) {
            e.printStackTrace();
        }

        return "forward:/logout";
    }

    @GetMapping({"id"})
    public String unblock(@PathVariable String id){

        User userById = userService.findUserById(Long.valueOf(id));
        userById.setStatus(Status.NORMAL);
        try {
            userService.saveUser(userById);
        } catch (SocialNetworkNotFoundException e) {
            e.printStackTrace();
        }

        return "home";
    }

    @DeleteMapping
    public String delete(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        userService.deleteUser(user.getId());

        return "logout";
    }


}
