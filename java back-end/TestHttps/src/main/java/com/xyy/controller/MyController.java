package com.xyy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WhiteRunner
 * @create 2021-11-12 21:23
 */
@RestController
public class MyController {

    @GetMapping("/hello")
    public String Hello(){
        return "hello world";
    }
}
