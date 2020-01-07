package com.obank.sign.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 */
@RestController
public class TestController {
    @GetMapping
    public void name() {
        Map<String, Object> map = new HashMap<>();

    }
}