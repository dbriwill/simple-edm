package fr.simple.ged.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController {

    @RequestMapping("/")
    public String home(Model model) {
    	model.addAttribute("section_document", true);
        return "/home";
    }

    
    @RequestMapping("/about")
    public String about(Model model) {
    	model.addAttribute("section_about", true);
        return "/home";
    }
}
