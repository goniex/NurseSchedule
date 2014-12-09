package com.nurseschedule.mvc.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class MainController {
    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userName = "";
        if ( authentication != null ) {
            userName = authentication.getName();
        }

        if ( userName.equals("admin@gmail.com") ) {
            return "admin_panel";
        } else {
            return "nurse_panel";
        }

    }
}