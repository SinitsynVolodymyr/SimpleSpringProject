package com.service;

import com.entity.Role;
import com.entity.SocialNetwork;
import com.entity.User;
import com.exception.SocialNetworkNotFoundException;
import com.repo.RoleRepository;
import com.repo.SocialNetworkRepository;
import com.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SocialNetworkService{

    @Autowired
    SocialNetworkRepository socialNetworkRepository;

    public SocialNetwork loadSocialNetworkByName(String socialNetworkName) throws SocialNetworkNotFoundException {
        SocialNetwork socialNetwork = socialNetworkRepository.findByName(socialNetworkName);

        if (socialNetwork == null) {
            throw new SocialNetworkNotFoundException("SocialNetwork "+socialNetworkName+" not found");
        }

        return socialNetwork;
    }

}
