package com.controller;

import com.entity.Status;
import com.entity.User;
import com.exception.SocialNetworkNotFoundException;
import com.exception.UserInDBNotFoundException;
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

    @PostMapping("block")
    public String block(@RequestBody Map<String,String> blockUsers) throws UserInDBNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (userService.isBlocked(userService.loadUserByAuth(auth))) return "redirect:/block";

        blockUsers.values().stream().forEach(userId -> {
            User user = userService.findUserById(Long.valueOf(userId));
            user.setStatus(statusRepository.findByName(Status.BLOCK.getName()));
            try {
                userService.updateUser(user);
            } catch (SocialNetworkNotFoundException e) {
                e.printStackTrace();
            }
        });

        return "redirect:/";
    }

    @PostMapping("unblock")
    public String unblock(@RequestBody Map<String,String> blockUsers) throws UserInDBNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (userService.isBlocked(userService.loadUserByAuth(auth))) return "redirect:/block";

        blockUsers.values().stream().forEach(userId -> {
            User user = userService.findUserById(Long.valueOf(userId));
            user.setStatus(statusRepository.findByName(Status.NORMAL.getName()));
            try {
                userService.updateUser(user);
            } catch (SocialNetworkNotFoundException e) {
                e.printStackTrace();
            }
        });

        return "redirect:/";
    }

    @PostMapping("delete")
    public String delete(@RequestBody Map<String,String> blockUsers) throws UserInDBNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserByAuth(auth);
        if (userService.isBlocked(user)) return "redirect:/block";
        blockUsers.values().stream().forEach(userId -> {
            userService.deleteUser(Long.valueOf(userId));
        });
        if (blockUsers.values().stream().filter(id-> user.getId().equals(Long.valueOf(id))).count()>0){
            return "redirect:/logout";
        }
        return "redirect:/";
    }


}
