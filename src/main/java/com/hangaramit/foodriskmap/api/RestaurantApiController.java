package com.hangaramit.foodriskmap.api;

import com.hangaramit.foodriskmap.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/api/restaurant")
@Controller
public class RestaurantApiController {

    @Autowired
    RestaurantService restaurantService;

//    @GetMapping("/add")
//    public String create() {
//        restaurantService.add();
//        return "redirect:/";
//    }
}


