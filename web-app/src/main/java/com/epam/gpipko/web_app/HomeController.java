package com.epam.gpipko.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String defaultPageRedirect() { return "redirect:projects"; }
}
