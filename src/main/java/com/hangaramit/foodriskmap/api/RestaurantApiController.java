package com.hangaramit.foodriskmap.api;

import com.hangaramit.foodriskmap.entity.Restaurant;
import com.hangaramit.foodriskmap.service.RestaurantService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/restaurant")
@RestController
public class RestaurantApiController {

    @Autowired
    RestaurantService restaurantService;

    //    @GetMapping("/add")
    //    public void create() {
    //        restaurantService.add();
    //    }
    @GetMapping("/show")
    public List<Restaurant> show() {
        return restaurantService.show();
    }
}


