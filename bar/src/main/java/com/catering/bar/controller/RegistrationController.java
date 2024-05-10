package com.catering.bar.controller;

import com.catering.bar.model.Roles;
import com.catering.bar.model.Users;
import com.catering.bar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/registration")
    public String getRegistrationPage(Model model){
        model.addAttribute("user", new Users());
        return "registration";
    }

    @PostMapping("/registration")
    public String request(@ModelAttribute("user") Users user, Model model){
        if (userRepository.findByUsername(user.getUsername()) != null){
            model.addAttribute("usernameError", "Такой логин уже существует!");
            return "registration";
        }
        if(!user.getPassword().equals(user.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароль не совпадают!");
            return "registration";
        }
        user.setRoles(Collections.singleton(new Roles(1L, "ROLE_BARMEN")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "registration";
    }
}
