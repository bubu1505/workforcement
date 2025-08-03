package com.ralise.hiring.workforcement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/g")
public class its {
    @GetMapping("/hel")
    public String mess(){
        return "hell brooo";
    }
}
