package com.config;

import com.entity.Role;
import com.entity.Status;
import com.entity.User;
import com.exception.SocialNetworkNotFoundException;
import com.repo.RoleRepository;
import com.repo.StatusRepository;
import com.repo.UserRepository;
import com.service.SocialNetworkService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;
    @Autowired
    SocialNetworkService socialNetworkService;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/js/**","/fonts/**","/images/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf()
                .disable()
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/login").not().fullyAuthenticated()
                //Все остальные страницы требуют аутентификации
                .anyRequest().authenticated()
                .and()
                //Настройка для входа в систему
                .formLogin()
                .loginPage("/login")
                .successForwardUrl("/home")
                //Перенарпавление на главную страницу после успешного входа
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");

/*
        httpSecurity
                .authorizeRequests()
                .mvcMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
                */
    }

    @Bean
    PrincipalExtractor principalExtractor(UserRepository userRepository){

        return map -> {
            String id = map.get("sub").toString();
            String name = map.get("name").toString();

            User user = userRepository.findBySocIdentifier(id).orElseGet(() -> {
                User result = new User(name);
                result.setSocIdentifier(id);
                result.setRegisteredDate(LocalDate.now());
                result.setRoles(Collections.singleton(roleRepository.findByName(Role.USER.getName())));
                result.setStatus(statusRepository.findByName(Status.NORMAL.getName()));
                try {
                    result.setSocialNetwork(socialNetworkService.loadSocialNetworkByName("google"));
                } catch (SocialNetworkNotFoundException e) {
                    return null;
                }
                return result;
            });
            user.setVisitDate(LocalDate.now());
            userRepository.save(user);
            return user;
        };
    }

}
