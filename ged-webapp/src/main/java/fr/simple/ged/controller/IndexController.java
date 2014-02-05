package fr.simple.ged.controller;

import javax.inject.Inject;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@PropertySources(value = {
		@PropertySource("classpath:/properties/constants.properties")
	}
)
public class IndexController {

    @Inject
    private Environment env;
	
    @RequestMapping("/")
    public String home(Model model) {
    	model.addAttribute("section_document", true);
    	model.addAttribute("APPLICATION_NAME", env.getProperty("APPLICATION_NAME"));
        return "/home";
    }

}
