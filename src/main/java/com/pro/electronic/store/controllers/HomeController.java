package com.pro.electronic.store.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//testing
@RestController
@RequestMapping("/test")
public class HomeController {

    @GetMapping
    public  String testing()
    {
        return  "WELCOME TO ELECTRO STORE.!!";
    }

}
