package com.catering.bar.controller;

import com.catering.bar.model.*;
import com.catering.bar.repository.DrinkRepository;
import com.catering.bar.repository.IngredientRepository;
import com.catering.bar.repository.StorageRepository;
import com.catering.bar.repository.UserRepository;
import com.catering.bar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class AdminController {

    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    DrinkRepository drinkRepository;
    @Autowired
    StorageRepository storageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @GetMapping("/admin")
    public String getAdminPage(Model model){
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("storage", new Storage());
        model.addAttribute("newIngredient", new Ingredient());
        model.addAttribute("newDrink", new Drink());
        model.addAttribute("ingredient", ingredientRepository.findAll());
        model.addAttribute("drink", drinkRepository.findAll());
        return "admin";
    }

    @PostMapping("/admin/addIngredient")
    public String addIngredient(@ModelAttribute("newIngredient") Ingredient ingredient){
        ingredientRepository.save(ingredient);
        return "redirect:/";
    }

    @PostMapping("/admin/addDrink")
    public String addDrink(@ModelAttribute("newDrink") Drink drink){
        drinkRepository.save(drink);
        return "redirect:/";
    }

    @PostMapping("/admin/addInStorage")
    public String addInStorage(@ModelAttribute("storage") Storage storage){
        storageRepository.save(storage);
        return "redirect:/";
    }

    @PostMapping("/admin/addDrinkIngredient")
    public String addDrinkIngredient(@RequestParam(required = true, defaultValue = "") Long drink,
                                     @RequestParam(required = true, defaultValue = "") Long ingredient){
        Ingredient addIngredient = ingredientRepository.findById(ingredient).get();
        Drink addDrink = drinkRepository.findById(drink).get();
        addDrink.getIngredients().add(addIngredient);
        drinkRepository.save(addDrink);
        return "redirect:/";
    }
    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam(required = true, defaultValue = "") Long userId,
                             Model model){
        Users user = userRepository.findById(userId).get();
        Set<Roles> roles = user.getRoles();
        if (userService.checkRole(user, roles)) {
            model.addAttribute("deleteError", "Администратор не может удалить администратора!");
            model.addAttribute("users", userRepository.findAll());
            return "admin";
        }
        userRepository.deleteById(userId);
        return "redirect:/admin";
    }
}
