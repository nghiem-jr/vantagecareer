package com.nghiemdd.vantagecareer.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloWord {

    @GetMapping("/")

    public String helloWord() {
        return "hello";
    }
}
