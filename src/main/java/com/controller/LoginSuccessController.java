package com.controller;

import com.entity.Role;
import com.entity.Status;
import com.entity.User;
import com.exception.SocialNetworkNotFoundException;
import com.repo.RoleRepository;
import com.repo.StatusRepository;
import com.repo.UserRepository;
import com.service.SocialNetworkService;
import com.service.UserService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("loginSuccess")
public class LoginSuccessController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    SocialNetworkService socialNetworkService;

    @GetMapping
    public String getPage(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        authUser(auth);

        return "redirect:/";
    }

    private User authUser(Authentication auth){
        OidcUserAuthority authority = (OidcUserAuthority) auth.getAuthorities().stream().findFirst().get();
        Map<String, Object> attributes = authority.getAttributes();
        String socName = ((OAuth2AuthenticationToken) auth).getAuthorizedClientRegistrationId();

        String id = attributes.get("sub").toString();
        String name = attributes.get("given_name").toString();

        User user = userRepository.findBySocIdentifier(id).orElseGet(() -> {
            return createNewUser(id, name, socName);
        });
        user.setVisitDate(LocalDate.now());
        userRepository.save(user);
        return user;

    }

    @Nullable
    private User createNewUser(String id, String name, String socName) {
        User result = new User(name);
        result.setSocIdentifier(id);
        result.setRegisteredDate(LocalDate.now());
        result.setRoles(Collections.singleton(roleRepository.findByName(Role.USER.getName())));
        result.setStatus(statusRepository.findByName(Status.NORMAL.getName()));
        try {
            result.setSocialNetwork(socialNetworkService.loadSocialNetworkByName(socName));
        } catch (SocialNetworkNotFoundException e) {
            return null;
        }
        return result;
    }


}
