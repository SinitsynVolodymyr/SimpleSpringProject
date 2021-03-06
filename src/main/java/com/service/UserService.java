package com.service;

import com.entity.Role;
import com.entity.SocialNetwork;
import com.entity.Status;
import com.entity.User;
import com.exception.SocialNetworkNotFoundException;
import com.exception.UserInDBNotFoundException;
import com.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SocialNetworkService socialNetworkService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean isBlocked(User user){
        Status status = user.getStatus();
        if (status.equals(Status.BLOCK)) return true;
        else return false;
    }


    public User loadUserBySocId(String socId) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findBySocIdentifier(socId);

        if (user.get() == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user.get();
    }

    public User loadUserByAuth(Authentication auth) throws UserInDBNotFoundException {
        Optional<User> user = userRepository.findBySocIdentifier(auth.getName());

        if (!user.isPresent() || user.get() == null) {
            throw new UserInDBNotFoundException();
        }

        return user.get();
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) throws SocialNetworkNotFoundException {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        SocialNetwork userSoc = user.getSocialNetwork();
        SocialNetwork socialNetworkFromBD = socialNetworkService.loadSocialNetworkByName(userSoc.getName());
        user.setSocialNetwork(socialNetworkFromBD);
        user.setRegisteredDate(LocalDate.now());
        userRepository.save(user);
        return true;
    }

    public boolean updateUser(User user) throws SocialNetworkNotFoundException {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB == null) {
            return false;
        }

        SocialNetwork userSoc = user.getSocialNetwork();
        SocialNetwork socialNetworkFromBD = socialNetworkService.loadSocialNetworkByName(userSoc.getName());
        user.setSocialNetwork(socialNetworkFromBD);
        user.setRegisteredDate(LocalDate.now());
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<User> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }
}
