package com.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    private int countUser = 14;

    private String messageFormat = "На сайте заригистрировалось %s человек.";

    @GetMapping
    public String unlogHome(Model model){
        model.addAttribute("amountUsers", String.format(messageFormat,countUser));
        ModelAndView modelAndView = new ModelAndView();

        return "unlogHome";
    }


}
