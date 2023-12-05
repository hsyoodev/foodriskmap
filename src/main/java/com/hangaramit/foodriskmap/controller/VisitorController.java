package com.hangaramit.foodriskmap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

public class VisitorController {

    @GetMapping("/board/writer")
    public String visitor(
            @RequestHeader("user-agent") String userAgent) {
        return userAgent;
    }

}
