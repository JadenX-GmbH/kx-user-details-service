package com.jadenx.kxuserdetailsservice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Hello World! Build 54 21.06.2021";
    }

    @GetMapping("/authenticated")
    @ResponseBody
    public String checkAuthenticated(final Principal user) {
        return "You're authenticated, " + user.getName() + "!";
    }

}
