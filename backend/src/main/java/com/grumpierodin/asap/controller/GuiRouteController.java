package com.grumpierodin.asap.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@CrossOrigin
public class GuiRouteController {
    @RequestMapping(value={"/about","/rules","/login", "/register","/home", "/event"}, method = RequestMethod.GET)
    public ModelAndView redirectApi() {
        return new ModelAndView("forward:/");
    }
}
