package com.controller;

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

import java.util.List;

@Controller
@RequestMapping("block")
public class BlockController {

    @GetMapping
    public String getPage(Model model) throws UserInDBNotFoundException {
        return "block";
    }


}
