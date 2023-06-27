package com.tyr.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author：carroll
 */
@Controller
@RequestMapping
public class LoginController {

    @GetMapping("/login")
    public String test() {
        return "login";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}
