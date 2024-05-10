package com.catering.bar.controller;

import com.catering.bar.model.*;
import com.catering.bar.repository.DrinkRepository;
import com.catering.bar.repository.IngredientRepository;
import com.catering.bar.repository.RequestRepository;
import com.catering.bar.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes({"request", "drink", "allRequest", "allStorage", "type"})
public class MainController {

    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    DrinkRepository drinkRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    StorageRepository storageRepository;

    @GetMapping("/")
    public String getMainPage(Model model){
        String[] type = {"Кофе", "Чай", "Напитки", "Энергетики", "Алкогольные напитки"};
        model.addAttribute("request", new Request());
        model.addAttribute("allRequest", requestRepository.findAll());
        model.addAttribute("type", type);
        return "home";
    }

    @GetMapping("/storage")
    public String storagePage(Model model){
        model.addAttribute("allStorage", storageRepository.findAll());
        return "storage";
    }

    @GetMapping("/ingredients")
    public String getComposition(Model model){
        model.addAttribute("drink", drinkRepository.findAll());
        return "ingredients";
    }

    @PostMapping("/addRequest")
    public String addRequest(@ModelAttribute("request") Request request,
                             @AuthenticationPrincipal Users user,
                             @RequestParam(required = true, defaultValue = "") Long drinkId){
        Drink drink = drinkRepository.findById(drinkId).get();
        request.setDrinkName(drink.getName());
        request.setStatus("Создана");
        request.setUserId(user.getId());
        requestRepository.save(request);
        return "redirect:/";
    }

    @PostMapping("/giveType")
    public String typeRequest(@RequestParam(required = true, defaultValue = "")String type,
                              Model model){
        List<Drink> drinks = drinkRepository.findByType(type);
        model.addAttribute("typeDrink", drinks);
        return "home";
    }

    @PostMapping("/request/giveAway")
    public String giveAway(@RequestParam(required = true, defaultValue = "") Long requestId){
        Request request = requestRepository.findById(requestId).get();
        request.setStatus("Отдан");
        requestRepository.save(request);
        return "redirect:/";
    }

    @PostMapping("/request/pay")
    public String payRequest(@RequestParam(required = true, defaultValue = "") Long requestId){
        Request request = requestRepository.findById(requestId).get();
        request.setStatus("Оплачен");
        requestRepository.save(request);
        return "redirect:/";
    }

    @PostMapping("/request/close")
    public String closeRequest(@RequestParam(required = true, defaultValue = "") Long requestId){
        Request request = requestRepository.findById(requestId).get();
        request.setStatus("Закрыт");
        requestRepository.save(request);
        return "redirect:/";
    }
}
