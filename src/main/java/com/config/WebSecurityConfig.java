package com.config;

import com.entity.User;
import com.exception.SocialNetworkNotFoundException;
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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;
    @Autowired
    SocialNetworkService socialNetworkService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()
                .disable()
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/login").not().fullyAuthenticated()
                //Доступ только для пользователей с ролью Администратор
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/info").hasAnyRole()
                //Все остальные страницы требуют аутентификации
                .anyRequest().authenticated()
                .and()
                //Настройка для входа в систему
                .formLogin()
                .loginPage("/login")
                //Перенарпавление на главную страницу после успешного входа
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");

    }

    @Bean
    PrincipalExtractor principalExtractor(UserRepository userRepository){

        return map -> {
            String id = map.get("sub").toString();
            String name = map.get("name").toString();

            User user = userRepository.findBySocIdentifier(id).orElseGet(() -> {
                User result = new User(name);
                result.setSocIdentifier(id);
                try {
                    result.setSocialNetwork(socialNetworkService.loadSocialNetworkByName("google"));
                } catch (SocialNetworkNotFoundException e) {
                    return null;
                }
                userRepository.save(result);
                return result;
            });
            return user;
        };
    }

}
