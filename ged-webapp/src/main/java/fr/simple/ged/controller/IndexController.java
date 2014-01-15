package fr.simple.ged.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class IndexController {

    @RequestMapping("/")
    public String home(Model model) {
        return "/home";
    }

}
